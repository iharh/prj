package myrequery

import io.kotlintest.specs.FunSpec

import org.postgresql.ds.PGSimpleDataSource
import oracle.jdbc.pool.OracleDataSource

//import io.requery.Persistable
import io.requery.query.Result

import io.requery.sql.Platform
import io.requery.sql.Configuration
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.KotlinEntityDataStore

import io.requery.reactivex.KotlinReactiveEntityStore

import io.requery.meta.EntityModel

import io.requery.kotlin.* // ?? something that brings up eq into scope

//import io.requery.kotlin.Selection
//import io.requery.kotlin.WhereAndOr
//import io.requery.kotlin.Conditional

import javax.sql.CommonDataSource

import java.util.concurrent.Executors
/*
https://github.com/requery/requery/blob/master/requery-kotlin/src/main/kotlin/io/requery/sql/KotlinEntityDataStore.kt
override infix fun <E : T> select(type: KClass<E>): Selection<Result<E>> {

https://github.com/requery/requery/blob/master/requery-kotlin/src/main/kotlin/io/requery/kotlin/Query.kt
interface Selection<E> : Distinct<DistinctSelection<E>>,
        From<E>,
        Join<E>,
        Where<E>,
        SetOperation<Selectable<E>>,
        GroupBy<SetHavingOrderByLimit<E>>,
        OrderBy<Limit<E>>,
Return<E>

interface Where<E> : SetGroupByOrderByLimit<E>, Return<E> {
    fun where(): Exists<SetGroupByOrderByLimit<E>>
    // !!!
    infix fun <V> where(condition: Condition<V, *>): WhereAndOr<E>
}

interface WhereAndOr<E> : AndOr<WhereAndOr<E>>, SetGroupByOrderByLimit<E>
*/

class ReQueryTest : FunSpec() {
    companion object {
	const val PRJ_ID = 2L // 20593L
	const val PROP_EXPORT_TARGET_DIR = "SENT.EXPORT.TARGET_DIR"
	const val PROP_EXPORT_TARGET_DIR_VAL = "\\\\freenas\\test1\\cmp-share1\\sent-exp"
    }

    init {
        test("String.length") {
            //val dataSource = PGSimpleDataSource() // dataSourceClass.newInstance()
            //dataSource.setDatabaseName("postgres") // diesel_demo
            //dataSource.setUser("win_ss") // postgres
            //dataSource.setPassword("")

            val dataSource = OracleDataSource() // dataSourceClass.newInstance()
	    dataSource.setURL("")
            dataSource.setUser("")
            dataSource.setPassword("")

            val model: EntityModel = Models.DEFAULT

            val configuration: Configuration = ConfigurationBuilder(dataSource, model)
                .useDefaultLogging()
                //.setEntityCache(new EmptyEntityCache())
                .setWriteExecutor(Executors.newSingleThreadExecutor())
                .build()

            //val data: KotlinReactiveEntityStore<Persistable> = KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(configuration))
            val data: KotlinEntityDataStore<Any> = KotlinEntityDataStore<Any>(configuration)

            data.invoke {
/*
                val p_new = PropEntity()
		p_new.projectId = PRJ_ID
		p_new.name = PROP_EXPORT_TARGET_DIR
		p_new.value = PROP_EXPORT_TARGET_DIR_VAL
		insert(p_new) // insert
*/
                val result = select(Prop::class) where((Prop::projectId eq PRJ_ID) and (Prop::name eq PROP_EXPORT_TARGET_DIR)) limit 1
                val p1: Prop = result.get().first()

                PRJ_ID shouldBe p1.projectId
                PROP_EXPORT_TARGET_DIR shouldBe p1.name
                PROP_EXPORT_TARGET_DIR_VAL shouldBe p1.value
            }
        }
    }
}
