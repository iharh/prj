error_chain! {
    foreign_links {
        IO(std::io::Error);
        Reqwest(reqwest::Error);
        ChannelSendError(crossbeam_channel::SendError<super::Message>);
        TryRecvError(crossbeam_channel::TryRecvError);
    }
}

pub type ResT<T> = Result<T>;
