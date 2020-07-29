package fr.neyox.client.common.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.common.io.ByteStreams;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IOUtil {
    public static String readInputStreamAsString(InputStream is) {
        String string;
        try {
            string = new String(ByteStreams.toByteArray(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return string;
    }

	public static void writeFromInputStream(FileOutputStream fileOutputStream, String jsonString) {
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			wr.write(jsonString);
			wr.flush();
			wr.close();
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
