                //val vvv: Long = 1
                //val r1:Selection<out Result<Post>> = select(Post::class)
                val result = select(Post::class) where(Post::id eq 2L) // limit 1

                val p1: Post = result.get().first()
                2L shouldBe p1.id
                "abc" shouldBe p1.title
                "abc def ggg" shouldBe p1.body

                val p2 = PostEntity()
                p2.id = 3
                p2.title = "t3"
                p2.body = "b3"

                insert(p2)
