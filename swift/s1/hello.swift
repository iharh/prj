#! /usr/bin/swift
import Foundation

print("Hello world")
let args = CommandLine.arguments
for a in args {
    print(a)
}
let fm = FileManager()
let curp: String = fm.currentDirectoryPath
print(curp)
let enumerator = fm.enumerator(atPath: curp) // -> FileManager.DirectoryEnumerator?
while let element = enumerator?.nextObject() as? String { 
//    // where element.pathExtension == "txt"
    print(element)
}
