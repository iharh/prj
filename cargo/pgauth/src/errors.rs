error_chain! {
    foreign_links {
        //IO(std::io::Error);
        PG(postgres::Error);
    }
}

pub type ResT<T> = Result<T>;
