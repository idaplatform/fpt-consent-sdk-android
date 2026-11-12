# R8 cho ban release cua thu vien: chi lam roi phan noi bo, giu nguyen API public ma app goi.
-keep public class vn.fpt.fis.cmp.consent.** {
    public protected *;
}
-keep class vn.fpt.fis.cmp.consent.model.** { *; }
-keep class vn.fpt.fis.cmp.consent.ConsentState { *; }

# Lop da lam roi nam trong package rieng cua SDK, tranh trung "a.a" voi thu vien khac trong app.
-repackageclasses 'vn.fpt.fis.cmp.consent.internal'

# Generic, inner class va annotation can cho app compile voi AAR; giu so dong de doc stack trace.
-keepattributes Signature,InnerClasses,EnclosingMethod,Exceptions,*Annotation*,SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
