package dk.apaq.shopsystem.rendering.resources;

import dk.apaq.shopsystem.util.StreamUtils;
import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.AbstractResource.WriteCallback;
import org.apache.wicket.request.resource.IResource.Attributes;

/**
 *
 * @author michael
 */
public class ResourceWriter extends WriteCallback {

        private final File file;

        private class ResponseOutputStream extends OutputStream {
    
        private final Response response;

        public ResponseOutputStream(Response response) {
            this.response = response;
        }

        @Override
        public void write(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            response.write(bytes);
        }

        @Override
        public void write(byte[] bytes, int i, int i1) throws IOException {
            write(Arrays.copyOfRange(bytes, i, i1));
        }
        
    
}

        public ResourceWriter(File file) {
            this.file = file;
        }
        
        
        @Override
        public void writeData(Attributes attributes) {
            try {
                StreamUtils.copy(file.getInputStream(), new ResponseOutputStream(attributes.getResponse()));
            } catch (IOException ex) {
                throw new WicketRuntimeException(ex);
            }
        }
    
}
