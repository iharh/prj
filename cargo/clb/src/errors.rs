error_chain! {
    foreign_links {
        IO(std::io::Error);
        Reqwest(reqwest::Error);
        DocOpt(docopt::Error);
    }
}

pub type ResT<T> = Result<T>;
