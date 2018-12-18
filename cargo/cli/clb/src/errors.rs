error_chain! {
    foreign_links {
        IO(std::io::Error);
        Reqwest(reqwest::Error);
        DocOpt(docopt::Error);
        TemplateFileError(handlebars::TemplateFileError);
        RenderError(handlebars::RenderError);
    }
}

pub type ResT<T> = Result<T>;
