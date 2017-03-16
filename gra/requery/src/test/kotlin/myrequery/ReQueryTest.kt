package myrequery

import io.kotlintest.specs.FunSpec

import org.postgresql.ds.PGSimpleDataSource

//import io.requery.Persistable
//import io.requery.query.Result

import io.requery.sql.Platform
import io.requery.sql.platform.PostgresSQL
import io.requery.sql.Configuration
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.KotlinEntityDataStore
import io.requery.reactivex.KotlinReactiveEntityStore

import io.requery.meta.EntityModel

import javax.sql.CommonDataSource

import java.util.concurrent.Executors

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

            // data
            //val eds: KotlinReactiveEntityStore<Persistable> = KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(configuration))
            val eds: KotlinEntityDataStore<Any> = KotlinEntityDataStore<Any>(configuration)

            eds.invoke {
                //val result = select(Post::id, Post::title) limit 1
                //val result = select(PostEntity::ID, PostEntity::TITLE) limit 1
                val result = select(Post::class) limit 2
                val first: Post = result.get().first()
                2 shouldBe first.id
                "abc" shouldBe first.title
                "abc def ggg" shouldBe first.body
            }
            //"".length shouldBe 0
        }
    }
}
