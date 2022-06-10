package at.universalnet.signature.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BLOBServiceImpl implements BLOBService {

	private static final Logger LOG = LoggerFactory.getLogger(BLOBServiceImpl.class);
	private static final String UNKNOWN_MIMETYPE = "application/octet-stream";

	private TikaConfig tika;

	public BLOBServiceImpl() {
		super();
		try {
			tika = new TikaConfig();
		} catch (TikaException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String detectMimeType(byte[] data) {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		Metadata md = new Metadata();
		try {
			MediaType mediaType = tika.getDetector().detect(is, md);
			return mediaType.getType() + "/" + mediaType.getSubtype();
		} catch (IOException e) {
			LOG.warn("Could not detect mime-type", e);
			return UNKNOWN_MIMETYPE;
		}
	}

}
