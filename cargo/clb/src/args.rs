pub const USAGE: &'static str = "
Helper for clb

Usage:
    clb <command> [<args>...]

Some common commands are:
    prj         create a project
    db          configure a database
";

#[derive(Debug, Deserialize)]
pub enum Command {
    Prj,
    Db,
}

#[derive(Debug, Deserialize)]
pub struct Args {
    pub arg_command: Command,
}
