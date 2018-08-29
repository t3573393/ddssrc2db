package org.fartpig.ddssrc2db.service;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.fartpig.ddssrc2db.pojo.lf.LFFileContent;
import org.fartpig.ddssrc2db.pojo.lf.LFLine;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.pf.PFFileContent;
import org.fartpig.ddssrc2db.pojo.pf.PFLine;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFFile;
import org.fartpig.ddssrc2db.resolver.LFResolver;
import org.fartpig.ddssrc2db.resolver.PFResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ResolverService {

	private static Logger log = LoggerFactory.getLogger(ResolverService.class);

	@Autowired
	private PFResolver pfResolver;

	@Autowired
	private LFResolver lfResolver;

	@Async
	public void resolve(String dirPath) {
		try {
			handlePFFile(pfResolver, dirPath);
			handleLFFile(lfResolver, dirPath);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handlePFFile(PFResolver pfResolver, String dirPath) throws CloneNotSupportedException {

		Collection<File> files = FileUtils.listFiles(new File(dirPath), new String[] { "PF" }, true);

		Map<String, PFFile> allPfFiles = new HashMap<String, PFFile>();
		for (File aFile : files) {
			log.info(aFile.getName());

			PFFileContent pfFileContent = pfResolver.parseFile(aFile);
			String fileBaseName = FilenameUtils.getBaseName(pfFileContent.getFileName());
			log.info("file type :" + pfFileContent.getFileType());
			for (PFLine line : pfFileContent.getLines()) {
				log.info(line.toString());
			}
			PFFile pfFile = pfResolver.convertToPFFile(pfFileContent);
			allPfFiles.put(fileBaseName, pfFile);
		}
		pfResolver.fillRefFiles(allPfFiles);
		pfResolver.shareFormat(allPfFiles);

		for (PFFile pfFile : allPfFiles.values()) {
			pfResolver.saveToDB(pfFile);
		}

		log.info("handle PFFile success !!! ");
	}

	private void handleLFFile(LFResolver lfResolver, String dirPath) throws CloneNotSupportedException {

		Collection<File> files = FileUtils.listFiles(new File(dirPath), new String[] { "LF" }, true);

		Map<String, LFFile> allLfFiles = new HashMap<String, LFFile>();
		for (File aFile : files) {
			log.info(aFile.getName());

			LFFileContent lfFileContent = lfResolver.parseFile(aFile);
			String fileBaseName = FilenameUtils.getBaseName(lfFileContent.getFileName());
			log.info("file type :" + lfFileContent.getFileType());
			for (LFLine line : lfFileContent.getLines()) {
				log.info(line.toString());
			}
			LFFile pfFile = lfResolver.convertToLFFile(lfFileContent);
			allLfFiles.put(fileBaseName, pfFile);
		}
		lfResolver.shareFormat(allLfFiles);

		for (LFFile lfFile : allLfFiles.values()) {
			lfResolver.deleteFromDB(lfFile);
			lfResolver.saveToDB(lfFile);
		}

		log.info("handle LFFile success !!! ");
	}

}
