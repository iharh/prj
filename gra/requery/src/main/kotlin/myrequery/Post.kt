package myrequery

import io.requery.*

@Entity // (name = "posts")
@Table(name = "posts")
interface Post { // : Persistable
    @get:Key
    //@get:Generated
    var id: Int

    @get:Column // (length = 10)
    var title: String

    @get:Column // (length = 10)
    var body: String

    @get:Column
    var published: Boolean
}
