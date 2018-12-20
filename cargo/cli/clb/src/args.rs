pub const USAGE: &'static str = "
Helper for clb

Usage:
    clb <command> [<args>...]

Some common commands are:
    prj         create a project
    realtime    process realtime request
";

#[derive(Debug, Deserialize)]
pub enum Command {
    Prj,
    RtSingle,
    RtMulti,
}

#[derive(Debug, Deserialize)]
pub struct Args {
    pub arg_command: Command,
}
