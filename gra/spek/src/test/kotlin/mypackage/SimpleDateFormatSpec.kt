package mypackage

import org.jetbrains.spek.subject.SubjectSpek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import java.text.SimpleDateFormat
import java.util.Date

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo

object SimpleDateFormatSpec: SubjectSpek<SimpleDateFormat>( {
    subject { SimpleDateFormat("yyyy") }

    describe("formatting") {
	it ("should format 4-digit year") {
	    assertThat("2017", equalTo(subject.format(Date())))
	}
    }
})
