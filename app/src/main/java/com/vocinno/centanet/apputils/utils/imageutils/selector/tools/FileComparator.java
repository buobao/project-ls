package com.vocinno.centanet.apputils.utils.imageutils.selector.tools;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import com.vocinno.centanet.apputils.utils.imageutils.selector.photos.NewImageItem;

//public class FileComparator implements Comparator<PhotoItem> {NewImageItem
public class FileComparator implements Comparator<NewImageItem> {	
	
	@Override
	public int compare(NewImageItem file1, NewImageItem file2) {
		if (file1.lastModified > file2.lastModified) {
			return -1;
		} else {
			return 1;
		}
	}

	public FileFilter fileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			String tmp = file.getName().toLowerCase();
			if (tmp.endsWith(".mov") || tmp.endsWith(".jpg")) {
				return true;
			}
			return false;
		}
	};
}
