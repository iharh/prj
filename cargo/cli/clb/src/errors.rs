error_chain! {
    foreign_links {
        IO(std::io::Error);
        Reqwest(reqwest::Error);
        DocOpt(docopt::Error);
        TemplateFileError(handlebars::TemplateFileError);
        RenderError(handlebars::RenderError);
        ChannelSendError(crossbeam_channel::SendError<super::Message>);
        TryRecvError(crossbeam_channel::TryRecvError);
    }
}

pub type ResT<T> = Result<T>;
