package posneg

import io.requery.*

// WORD        CHARACTER VARYING(256) NOT NULL,
// SENTIMENT   NUMERIC(1,0) DEFAULT 0 NOT NULL,
// LANGUAGE_ID CHARACTER(2) NOT NULL,

@Entity
@Table(name = "pu_positive_negative")
interface PosNeg {
    //@get:Key
    @get:Column(name = "word", length = 255, nullable = false)
    var word: String

    @get:Column(name = "sentiment", nullable = false)
    var sentiment: Byte
}
