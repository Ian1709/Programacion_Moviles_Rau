package com.rau.cafecampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rau.cafecampus.data.local.dao.PedidoDao
import com.rau.cafecampus.data.local.dao.ProductoDao
import com.rau.cafecampus.data.local.dao.UsuarioDao
import com.rau.cafecampus.data.local.entity.DetallePedido
import com.rau.cafecampus.data.local.entity.Pedido
import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.local.entity.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        Pedido::class,
        DetallePedido::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CafeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CafeDatabase::class.java,
                    "cafecampus_database"
                )
                    .addCallback(CafeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CafeDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        poblarBaseDeDatos(database)
                    }
                }
            }

            suspend fun poblarBaseDeDatos(database: CafeDatabase) {
                val usuarioDao = database.usuarioDao()
                val productoDao = database.productoDao()

                DatosIniciales.usuariosIniciales.forEach { usuario ->
                    usuarioDao.insertar(usuario)
                }

                productoDao.insertarTodos(DatosIniciales.productosIniciales)
            }
        }
    }
}
