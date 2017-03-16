package myrequery

import io.kotlintest.specs.FunSpec

import org.postgresql.ds.PGSimpleDataSource

//import io.requery.Persistable

import io.requery.query.Result

import io.requery.sql.Platform
import io.requery.sql.Configuration
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.platform.PostgresSQL

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
    init {
        test("String.length") {
            val platform: Platform = PostgresSQL()
            val dataSource = PGSimpleDataSource() // dataSourceClass.newInstance()
            dataSource.setUser("postgres")
            dataSource.setDatabaseName("diesel_demo")

            val model: EntityModel = Models.DEFAULT

            val configuration: Configuration = ConfigurationBuilder(dataSource, model)
                .setPlatform(platform)
                .useDefaultLogging()
                //.setEntityCache(new EmptyEntityCache())
                .setWriteExecutor(Executors.newSingleThreadExecutor())
                .build()

            //val data: KotlinReactiveEntityStore<Persistable> = KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(configuration))
            val data: KotlinEntityDataStore<Any> = KotlinEntityDataStore<Any>(configuration)

            data.invoke {
                //val vvv: Long = 1
                //val r1:Selection<out Result<Post>> = select(Post::class)
                val result = select(Post::class) where(Post::id eq 2L) // limit 1

                val p1: Post = result.get().first()
                2L shouldBe p1.id
                "abc" shouldBe p1.title
                "abc def ggg" shouldBe p1.body

                //val 
            }
            //"".length shouldBe 0
        }
    }
}
