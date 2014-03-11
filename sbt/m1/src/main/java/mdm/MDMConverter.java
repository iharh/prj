package mdm;

import mdm.xstream.MDMInStreamFactory;
import mdm.xstream.MDMOutStreamFactory;

import mdm.in.InDoc;
import mdm.out.OutDoc;

import java.io.Reader;
import java.io.Writer;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import static mdm.xstream.MDMXStreamConstants.*;

public class MDMConverter {

    public static void marshal(InDoc inDoc, Writer writer) throws IOException {
        try (ObjectOutputStream out = MDMInStreamFactory.getXStream().createObjectOutputStream(writer, ROOT_EL_IN)) {
            out.writeObject(inDoc);
        }
    }

    public static OutDoc unmarshal(Reader reader) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = MDMOutStreamFactory.getXStream().createObjectInputStream(reader)) {
            return (OutDoc) in.readObject();
        }
    }
};
