package posneg

import io.kotlintest.specs.FunSpec
import io.kotlintest.matchers.shouldBe

import com.natpryce.konfig.*

import org.postgresql.ds.PGSimpleDataSource

//import io.requery.Persistable
import io.requery.query.Result

import io.requery.sql.Platform
import io.requery.sql.Configuration
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.KotlinEntityDataStore

import io.requery.reactivex.KotlinReactiveEntityStore

import io.requery.meta.EntityModel

// ?? something that brings up PropertyExtensions into scope
//import io.requery.kotlin.* 
import io.requery.kotlin.Selection
import io.requery.kotlin.WhereAndOr
import io.requery.kotlin.Conditional
import io.requery.kotlin.eq

import javax.sql.CommonDataSource

import java.io.File

import java.util.concurrent.Executors

//import mu.KLogging
import mu.KLoggable
import mu.NamedKLogging
//import mu.KotlinLogging

//private val log = KotlinLogging.logger {} 

class ReQueryTest : FunSpec() {
    //companion object: KLogging()
    companion object: Any(), KLoggable by NamedKLogging("posneg.ReQueryTest")

    /*companion object {
	const val PRJ_ID = 0L
	const val PROP_EXPORT_TARGET_DIR = "SENT.EXPORT.TARGET_DIR"
	const val PROP_EXPORT_TARGET_DIR_VAL = "D:\\clb\\inst/exportedSentiment"
    }*/

    object cmpDS : PropertyGroup() {
	val name by stringType // database name
	val username by stringType // intType
	val password by stringType
    }

    init {
        test("pu_positive_negative.export") {
	    logger.info("abc")
	    /*
	    val config = ConfigurationProperties.systemProperties() overriding
		 EnvironmentVariables() overriding
		 ConfigurationProperties.fromFile(File("database.system.properties"));

            val dataSource = PGSimpleDataSource()
            dataSource.setDatabaseName(config[cmpDS.name])
            dataSource.setUser(config[cmpDS.username])
            dataSource.setPassword(config[cmpDS.password])

            val model: EntityModel = Models.DEFAULT

            val configuration: Configuration = ConfigurationBuilder(dataSource, model)
                .useDefaultLogging()
                //.setEntityCache(new EmptyEntityCache())
                .setWriteExecutor(Executors.newSingleThreadExecutor())
                .build()

            //val data: KotlinReactiveEntityStore<Persistable> = KotlinReactiveEntityStore<Persistable>(KotlinEntityDataStore(configuration))
            val data: KotlinEntityDataStore<Any> = KotlinEntityDataStore<Any>(configuration)

            data.invoke {
                val result = select(PosNeg::class)
                val posnegs = result.get()

                //PRJ_ID shouldBe p1.projectId
                //PROP_EXPORT_TARGET_DIR shouldBe p1.name
                //PROP_EXPORT_TARGET_DIR_VAL shouldBe p1.value
            }
	    */
        }
    }
}
