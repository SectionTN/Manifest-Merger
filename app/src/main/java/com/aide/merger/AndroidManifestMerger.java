package com.aide.merger;

import com.android.manifmerger.ManifestMerger2;
import com.android.manifmerger.MergingReport;
import com.android.utils.ILogger;

import java.io.File;
import java.io.FileWriter;

public class AndroidManifestMerger {
    public static String merge(String outputManifestPath, String manifestPath, String[] variantManifestPaths, String[] libManifestPaths) {
        File[] libraryFiles = new File[libManifestPaths.length];
        for (int i = 0; i < libManifestPaths.length; i++) {
            libraryFiles[i] = new File(libManifestPaths[i]);
        }
        File[] variantFiles = new File[variantManifestPaths.length];
        for (int i2 = 0; i2 < variantManifestPaths.length; i2++) {
            variantFiles[i2] = new File(variantManifestPaths[i2]);
        }
        try {
            MergingReport report = ManifestMerger2.newMerger(new File(manifestPath), new ILogger() {
                @Override
                public void error(Throwable t, String msgFormat, Object... args) {
                }

                @Override
                public void warning(String msgFormat, Object... args) {
                }

                @Override
                public void info(String msgFormat, Object... args) {
                }

                @Override
                public void verbose(String msgFormat, Object... args) {
                }
            }, ManifestMerger2.MergeType.APPLICATION).addFlavorAndBuildTypeManifests(variantFiles).addLibraryManifests(libraryFiles).withFeatures(ManifestMerger2.Invoker.Feature.MAKE_AAPT_SAFE, ManifestMerger2.Invoker.Feature.REMOVE_TOOLS_DECLARATIONS).merge();
            if (!report.getResult().isSuccess()) {
                StringBuilder b = new StringBuilder();
                for (MergingReport.Record record : report.getLoggingRecords()) {
                    b.append("Manifest merge problem: ").append(record.toString().replace((char) 10, ' '));
                    b.append("\n");
                }
                return b.toString();
            } else if (report.getMergedDocument(MergingReport.MergedManifestKind.AAPT_SAFE) == null) {
                return "Failed to merge manifest";
            } else {
                String content = report.getMergedDocument(MergingReport.MergedManifestKind.AAPT_SAFE);
                new File(outputManifestPath).getParentFile().mkdirs();
                FileWriter writer = new FileWriter(outputManifestPath);
                writer.write(content);
                writer.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}