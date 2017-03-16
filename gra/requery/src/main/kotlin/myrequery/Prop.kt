package myrequery

import io.requery.*

@Entity
@Table(name = "cb_properties")
interface Prop {
    //@get:Key
    @get:Column(name = "id_project", nullable = false)
    var projectId: Long

    @get:Column(name = "prop_name", length = 255, nullable = false)
    var name: String

    @get:Column(name = "prop_value", length = 4000)
    var value: String
}
