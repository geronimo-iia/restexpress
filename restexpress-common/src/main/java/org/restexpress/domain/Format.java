/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.restexpress.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link Format} define constant to manage {format} parameter.
 * 
 * @author <a href(""mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Format {
    LOTUS("123", "application/vnd.lotus-1-2-3"), //
    IN3DML("3dml", "text/vnd.in3d.3dml"), //
    VIDEO_3G2("3g2", "video/3gpp2"), //
    VIDEO_3GP("3gp", "video/3gpp"), //
    VIDEO_7Z("7z", "application/x-7z-compressed"), //
    AAB("aab", "application/x-authorware-bin"), //
    AAC("aac", "audio/x-aac"), //
    AAM("aam", "application/x-authorware-map"), //
    A("a", "application/octet-stream"), //
    AAS("aas", "application/x-authorware-seg"), //
    ABS("abs", "audio/x-mpeg"), //
    ABW("abw", "application/x-abiword"), //
    AC("ac", "application/pkix-attr-cert"), //
    ACC("acc", "application/vnd.americandynamics.acc"), //
    ACE("ace", "application/x-ace-compressed"), //
    ACU("acu", "application/vnd.acucobol"), //
    ACUTC("acutc", "application/vnd.acucorp"), //
    ADP("adp", "audio/adpcm"), //
    AEP("aep", "application/vnd.audiograph"), //
    AFM("afm", "application/x-font-type1"), //
    AFP("afp", "application/vnd.ibm.modcap"), //
    AHEAD("ahead", "application/vnd.ahead.space"), //
    AI("ai", "application/postscript"), //
    AIF("aif", "audio/x-aiff"), //
    AIFC("aifc", "audio/x-aiff"), //
    AIFF("aiff", "audio/x-aiff"), //
    AIM("aim", "application/x-aim"), //
    AIR("air", "application/vnd.adobe.air-application-installer-package+zip"), //
    AIT("ait", "application/vnd.dvb.ait"), //
    AMI("ami", "application/vnd.amiga.ami"), //
    ANX("anx", "application/annodex"), //
    APK("apk", "application/vnd.android.package-archive"), //
    APPLICATION("application", "application/x-ms-application"), //
    APR("apr", "application/vnd.lotus-approach"), //
    ART("art", "image/x-jg"), //
    ASC("asc", "application/pgp-signature"), //
    ASF("asf", "video/x-ms-asf"), //
    ASM("asm", "text/x-asm"), //
    ASO("aso", "application/vnd.accpac.simply.aso"), //
    ASX("asx", "video/x-ms-asf"), //
    ATC("atc", "application/vnd.acucorp"), //
    ATOM("atom", "application/atom+xml"), //
    ATOMCAT("atomcat", "application/atomcat+xml"), //
    ATOMSVC("atomsvc", "application/atomsvc+xml"), //
    ATX("atx", "application/vnd.antix.game-component"), //
    AU("au", "audio/basic"), //
    AVI("avi", "video/x-msvideo"), //
    AVX("avx", "video/x-rad-screenplay"), //
    AW("aw", "application/applixware"), //
    AXA("axa", "audio/annodex"), //
    AXV("axv", "video/annodex"), //
    AZF("azf", "application/vnd.airzip.filesecure.azf"), //
    AZS("azs", "application/vnd.airzip.filesecure.azs"), //
    AZW("azw", "application/vnd.amazon.ebook"), //
    BAT("bat", "application/x-msdownload"), //
    BCPIO("bcpio", "application/x-bcpio"), //
    BDF("bdf", "application/x-font-bdf"), //
    BDM("bdm", "application/vnd.syncml.dm+wbxml"), //
    BED("bed", "application/vnd.realvnc.bed"), //
    BH2("bh2", "application/vnd.fujitsu.oasysprs"), //
    BIN("bin", "application/octet-stream"), //
    BMI("bmi", "application/vnd.bmi"), //
    BMP("bmp", "image/bmp"), //
    BODY("body", "text/html"), //
    BOOK("book", "application/vnd.framemaker"), //
    BOX("box", "application/vnd.previewsystems.box"), //
    BOZ("boz", "application/x-bzip2"), //
    BPK("bpk", "application/octet-stream"), //
    BTIF("btif", "image/prs.btif"), //
    BZ2("bz2", "application/x-bzip2"), //
    BZ("bz", "application/x-bzip"), //
    C11AMC("c11amc", "application/vnd.cluetrust.cartomobile-config"), //
    C11AMZ("c11amz", "application/vnd.cluetrust.cartomobile-config-pkg"), //
    C4D("c4d", "application/vnd.clonk.c4group"), //
    C4F("c4f", "application/vnd.clonk.c4group"), //
    C4G("c4g", "application/vnd.clonk.c4group"), //
    C4P("c4p", "application/vnd.clonk.c4group"), //
    C4U("c4u", "application/vnd.clonk.c4group"), //
    CAB("cab", "application/vnd.ms-cab-compressed"), //
    CAP("cap", "application/vnd.tcpdump.pcap"), //
    CAR("car", "application/vnd.curl.car"), //
    CAT("cat", "application/vnd.ms-pki.seccat"), //
    CCT("cct", "application/x-director"), //
    CC("cc", "text/x-c"), //
    CCXML("ccxml", "application/ccxml+xml"), //
    CDBCMSG("cdbcmsg", "application/vnd.contact.cmsg"), //
    CDF("cdf", "application/x-cdf"), //
    CDKEY("cdkey", "application/vnd.mediastation.cdkey"), //
    CDMIA("cdmia", "application/cdmi-capability"), //
    CDMIC("cdmic", "application/cdmi-container"), //
    CDMID("cdmid", "application/cdmi-domain"), //
    CDMIO("cdmio", "application/cdmi-object"), //
    CDMIQ("cdmiq", "application/cdmi-queue"), //
    CDX("cdx", "chemical/x-cdx"), //
    CDXML("cdxml", "application/vnd.chemdraw+xml"), //
    CDY("cdy", "application/vnd.cinderella"), //
    CER("cer", "application/pkix-cert"), //
    CGM("cgm", "image/cgm"), //
    CHAT("chat", "application/x-chat"), //
    CHM("chm", "application/vnd.ms-htmlhelp"), //
    CHRT("chrt", "application/vnd.kde.kchart"), //
    CIF("cif", "chemical/x-cif"), //
    CII("cii", "application/vnd.anser-web-certificate-issue-initiation"), //
    CIL("cil", "application/vnd.ms-artgalry"), //
    CLA("cla", "application/vnd.claymore"), //
    CLASS("class", "application/java"), //
    CLKK("clkk", "application/vnd.crick.clicker.keyboard"), //
    CLKP("clkp", "application/vnd.crick.clicker.palette"), //
    CLKT("clkt", "application/vnd.crick.clicker.template"), //
    CLKW("clkw", "application/vnd.crick.clicker.wordbank"), //
    CLKX("clkx", "application/vnd.crick.clicker"), //
    CLP("clp", "application/x-msclip"), //
    CMC("cmc", "application/vnd.cosmocaller"), //
    CMDF("cmdf", "chemical/x-cmdf"), //
    CML("cml", "chemical/x-cml"), //
    CMP("cmp", "application/vnd.yellowriver-custom-menu"), //
    CMX("cmx", "image/x-cmx"), //
    COD("cod", "application/vnd.rim.cod"), //
    COM("com", "application/x-msdownload"), //
    CONF("conf", "text/plain"), //
    CPIO("cpio", "application/x-cpio"), //
    CPP("cpp", "text/x-c"), //
    CPT("cpt", "application/mac-compactpro"), //
    CRD("crd", "application/x-mscardfile"), //
    CRL("crl", "application/pkix-crl"), //
    CRT("crt", "application/x-x509-ca-cert"), //
    CRYPTONOTE("cryptonote", "application/vnd.rig.cryptonote"), //
    CSH("csh", "application/x-csh"), //
    CSML("csml", "chemical/x-csml"), //
    CSP("csp", "application/vnd.commonspace"), //
    CSS("css", "text/css"), //
    CST("cst", "application/x-director"), //
    CSV("csv", "text/csv"), //
    C("c", "text/x-c"), //
    CU("cu", "application/cu-seeme"), //
    CURL("curl", "text/vnd.curl"), //
    CWW("cww", "application/prs.cww"), //
    CXT("cxt", "application/x-director"), //
    CXX("cxx", "text/x-c"), //
    DAE("dae", "model/vnd.collada+xml"), //
    DAF("daf", "application/vnd.mobius.daf"), //
    DATALESS("dataless", "application/vnd.fdsn.seed"), //
    DAVMOUNT("davmount", "application/davmount+xml"), //
    DCR("dcr", "application/x-director"), //
    DCURL("dcurl", "text/vnd.curl.dcurl"), //
    DD2("dd2", "application/vnd.oma.dd2+xml"), //
    DDD("ddd", "application/vnd.fujixerox.ddd"), //
    DEB("deb", "application/x-debian-package"), //
    DEF("def", "text/plain"), //
    DEPLOY("deploy", "application/octet-stream"), //
    DER("der", "application/x-x509-ca-cert"), //
    DFAC("dfac", "application/vnd.dreamfactory"), //
    DIB("dib", "image/bmp"), //
    DIC("dic", "text/x-c"), //
    DIFF("diff", "text/plain"), //
    DIR("dir", "application/x-director"), //
    DIS("dis", "application/vnd.mobius.dis"), //
    DIST("dist", "application/octet-stream"), //
    DISTZ("distz", "application/octet-stream"), //
    DJV("djv", "image/vnd.djvu"), //
    DJVU("djvu", "image/vnd.djvu"), //
    DLL("dll", "application/x-msdownload"), //
    DMG("dmg", "application/octet-stream"), //
    DMP("dmp", "application/vnd.tcpdump.pcap"), //
    DMS("dms", "application/octet-stream"), //
    DNA("dna", "application/vnd.dna"), //
    DOC("doc", "application/msword"), //
    DOCM("docm", "application/vnd.ms-word.document.macroenabled.12"), //
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), //
    DOT("dot", "application/msword"), //
    DOTM("dotm", "application/vnd.ms-word.template.macroenabled.12"), //
    DOTX("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template"), //
    DP("dp", "application/vnd.osgi.dp"), //
    DPG("dpg", "application/vnd.dpgraph"), //
    DRA("dra", "audio/vnd.dra"), //
    DSC("dsc", "text/prs.lines.tag"), //
    DSSC("dssc", "application/dssc+der"), //
    DTB("dtb", "application/x-dtbook+xml"), //
    DTD("dtd", "application/xml-dtd"), //
    DTS("dts", "audio/vnd.dts"), //
    DTSHD("dtshd", "audio/vnd.dts.hd"), //
    DUMP("dump", "application/octet-stream"), //
    DVB("dvb", "video/vnd.dvb.file"), //
    DVI("dvi", "application/x-dvi"), //
    DV("dv", "video/x-dv"), //
    DWF("dwf", "model/vnd.dwf"), //
    DWG("dwg", "image/vnd.dwg"), //
    DXF("dxf", "image/vnd.dxf"), //
    DXP("dxp", "application/vnd.spotfire.dxp"), //
    DXR("dxr", "application/x-director"), //
    EAR("ear", "application/octet-stream"), //
    ECELP4800("ecelp4800", "audio/vnd.nuera.ecelp4800"), //
    ECELP7470("ecelp7470", "audio/vnd.nuera.ecelp7470"), //
    ECELP9600("ecelp9600", "audio/vnd.nuera.ecelp9600"), //
    ECMA("ecma", "application/ecmascript"), //
    EDM("edm", "application/vnd.novadigm.edm"), //
    EDX("edx", "application/vnd.novadigm.edx"), //
    EFIF("efif", "application/vnd.picsel"), //
    EI6("ei6", "application/vnd.pg.osasli"), //
    ELC("elc", "application/octet-stream"), //
    EML("eml", "message/rfc822"), //
    EMMA("emma", "application/emma+xml"), //
    EOL("eol", "audio/vnd.digital-winds"), //
    EOT("eot", "application/vnd.ms-fontobject"), //
    EPS("eps", "application/postscript"), //
    EPUB("epub", "application/epub+zip"), //
    ES3("es3", "application/vnd.eszigno3+xml"), //
    ESF("esf", "application/vnd.epson.esf"), //
    ET3("et3", "application/vnd.eszigno3+xml"), //
    ETX("etx", "text/x-setext"), //
    EXE("exe", "application/x-msdownload"), //
    EXI("exi", "application/exi"), //
    EXT("ext", "application/vnd.novadigm.ext"), //
    EZ2("ez2", "application/vnd.ezpix-album"), //
    EZ3("ez3", "application/vnd.ezpix-package"), //
    EZ("ez", "application/andrew-inset"), //
    F4V("f4v", "video/x-f4v"), //
    F77("f77", "text/x-fortran"), //
    F90("f90", "text/x-fortran"), //
    FBS("fbs", "image/vnd.fastbidsheet"), //
    FCS("fcs", "application/vnd.isac.fcs"), //
    FDF("fdf", "application/vnd.fdf"), //
    FE_LAUNCH("fe_launch", "application/vnd.denovo.fcselayout-link"), //
    FG5("fg5", "application/vnd.fujitsu.oasysgp"), //
    FGD("fgd", "application/x-director"), //
    FH4("fh4", "image/x-freehand"), //
    FH5("fh5", "image/x-freehand"), //
    FH7("fh7", "image/x-freehand"), //
    FHC("fhc", "image/x-freehand"), //
    FH("fh", "image/x-freehand"), //
    FIG("fig", "application/x-xfig"), //
    FLAC("flac", "audio/flac"), //
    FLI("fli", "video/x-fli"), //
    FLO("flo", "application/vnd.micrografx.flo"), //
    FLV("flv", "video/x-flv"), //
    FLW("flw", "application/vnd.kde.kivio"), //
    FLX("flx", "text/vnd.fmi.flexstor"), //
    FLY("fly", "text/vnd.fly"), //
    FM("fm", "application/vnd.framemaker"), //
    FNC("fnc", "application/vnd.frogans.fnc"), //
    FOR("for", "text/x-fortran"), //
    FPX("fpx", "image/vnd.fpx"), //
    FRAME("frame", "application/vnd.framemaker"), //
    FSC("fsc", "application/vnd.fsc.weblaunch"), //
    FST("fst", "image/vnd.fst"), //
    FTC("ftc", "application/vnd.fluxtime.clip"), //
    F("f", "text/x-fortran"), //
    FTI("fti", "application/vnd.anser-web-funds-transfer-initiation"), //
    FVT("fvt", "video/vnd.fvt"), //
    FXP("fxp", "application/vnd.adobe.fxp"), //
    FXPL("fxpl", "application/vnd.adobe.fxp"), //
    FZS("fzs", "application/vnd.fuzzysheet"), //
    G2W("g2w", "application/vnd.geoplan"), //
    G3("g3", "image/g3fax"), //
    G3W("g3w", "application/vnd.geospace"), //
    GAC("gac", "application/vnd.groove-account"), //
    GBR("gbr", "application/rpki-ghostbusters"), //
    GDL("gdl", "model/vnd.gdl"), //
    GEO("geo", "application/vnd.dynageo"), //
    GEX("gex", "application/vnd.geometry-explorer"), //
    GGB("ggb", "application/vnd.geogebra.file"), //
    GGT("ggt", "application/vnd.geogebra.tool"), //
    GHF("ghf", "application/vnd.groove-help"), //
    GIF("gif", "image/gif"), //
    GIM("gim", "application/vnd.groove-identity-message"), //
    GMX("gmx", "application/vnd.gmx"), //
    GNUMERIC("gnumeric", "application/x-gnumeric"), //
    GPH("gph", "application/vnd.flographit"), //
    GQF("gqf", "application/vnd.grafeq"), //
    GQS("gqs", "application/vnd.grafeq"), //
    GRAM("gram", "application/srgs"), //
    GRE("gre", "application/vnd.geometry-explorer"), //
    GRV("grv", "application/vnd.groove-injector"), //
    GRXML("grxml", "application/srgs+xml"), //
    GSF("gsf", "application/x-font-ghostscript"), //
    GTAR("gtar", "application/x-gtar"), //
    GTM("gtm", "application/vnd.groove-tool-message"), //
    GTW("gtw", "model/vnd.gtw"), //
    GV("gv", "text/vnd.graphviz"), //
    GXT("gxt", "application/vnd.geonext"), //
    GZ("gz", "application/x-gzip"), //
    H261("h261", "video/h261"), //
    H263("h263", "video/h263"), //
    H264("h264", "video/h264"), //
    HAL("hal", "application/vnd.hal+xml"), //
    HBCI("hbci", "application/vnd.hbci"), //
    HDF("hdf", "application/x-hdf"), //
    HH("hh", "text/x-c"), //
    HLP("hlp", "application/winhlp"), //
    HPGL("hpgl", "application/vnd.hp-hpgl"), //
    HPID("hpid", "application/vnd.hp-hpid"), //
    HPS("hps", "application/vnd.hp-hps"), //
    HQX("hqx", "application/mac-binhex40"), //
    HTC("htc", "text/x-component"), //
    H("h", "text/x-c"), //
    HTKE("htke", "application/vnd.kenameaapp"), //
    HTML("html", "text/html"), //
    HTM("htm", "text/html"), //
    HVD("hvd", "application/vnd.yamaha.hv-dic"), //
    HVP("hvp", "application/vnd.yamaha.hv-voice"), //
    HVS("hvs", "application/vnd.yamaha.hv-script"), //
    I2G("i2g", "application/vnd.intergeo"), //
    ICC("icc", "application/vnd.iccprofile"), //
    ICE("ice", "x-conference/x-cooltalk"), //
    ICM("icm", "application/vnd.iccprofile"), //
    ICO("ico", "image/x-icon"), //
    ICS("ics", "text/calendar"), //
    IEF("ief", "image/ief"), //
    IFB("ifb", "text/calendar"), //
    IFM("ifm", "application/vnd.shana.informed.formdata"), //
    IGES("iges", "model/iges"), //
    IGL("igl", "application/vnd.igloader"), //
    IGM("igm", "application/vnd.insors.igm"), //
    IGS("igs", "model/iges"), //
    IGX("igx", "application/vnd.micrografx.igx"), //
    IIF("iif", "application/vnd.shana.informed.interchange"), //
    IMP("imp", "application/vnd.accpac.simply.imp"), //
    IMS("ims", "application/vnd.ms-ims"), //
    INK("ink", "application/inkml+xml"), //
    INKML("inkml", "application/inkml+xml"), //
    IN("in", "text/plain"), //
    IOTA("iota", "application/vnd.astraea-software.iota"), //
    IPA("ipa", "application/octet-stream"), //
    IPFIX("ipfix", "application/ipfix"), //
    IPK("ipk", "application/vnd.shana.informed.package"), //
    IRM("irm", "application/vnd.ibm.rights-management"), //
    IRP("irp", "application/vnd.irepository.package+xml"), //
    ISO("iso", "application/octet-stream"), //
    ITP("itp", "application/vnd.shana.informed.formtemplate"), //
    IVP("ivp", "application/vnd.immervision-ivp"), //
    IVU("ivu", "application/vnd.immervision-ivu"), //
    JAD("jad", "text/vnd.sun.j2me.app-descriptor"), //
    JAM("jam", "application/vnd.jam"), //
    JAR("jar", "application/java-archive"), //
    JAVA("java", "text/x-java-source"), //
    JISP("jisp", "application/vnd.jisp"), //
    JLT("jlt", "application/vnd.hp-jlyt"), //
    JNLP("jnlp", "application/x-java-jnlp-file"), //
    JODA("joda", "application/vnd.joost.joda-archive"), //
    JPEG("jpeg", "image/jpeg"), //
    JPE("jpe", "image/jpeg"), //
    JPG("jpg", "image/jpeg"), //
    JPGM("jpgm", "video/jpm"), //
    JPGV("jpgv", "video/jpeg"), //
    JPM("jpm", "video/jpm"), //
    JS("js", "application/javascript"), //
    JSF("jsf", "text/plain"), //
    JSON("json", "application/json"), //
    JSPF("jspf", "text/plain"), //
    KAR("kar", "audio/midi"), //
    KARBON("karbon", "application/vnd.kde.karbon"), //
    KFO("kfo", "application/vnd.kde.kformula"), //
    KIA("kia", "application/vnd.kidspiration"), //
    KIL("kil", "application/x-killustrator"), //
    KML("kml", "application/vnd.google-earth.kml+xml"), //
    KMZ("kmz", "application/vnd.google-earth.kmz"), //
    KNE("kne", "application/vnd.kinar"), //
    KNP("knp", "application/vnd.kinar"), //
    KON("kon", "application/vnd.kde.kontour"), //
    KPR("kpr", "application/vnd.kde.kpresenter"), //
    KPT("kpt", "application/vnd.kde.kpresenter"), //
    KSH("ksh", "text/plain"), //
    KSP("ksp", "application/vnd.kde.kspread"), //
    KTR("ktr", "application/vnd.kahootz"), //
    KTX("ktx", "image/ktx"), //
    KTZ("ktz", "application/vnd.kahootz"), //
    KWD("kwd", "application/vnd.kde.kword"), //
    KWT("kwt", "application/vnd.kde.kword"), //
    LASXML("lasxml", "application/vnd.las.las+xml"), //
    LATEX("latex", "application/x-latex"), //
    LBD("lbd", "application/vnd.llamagraphics.life-balance.desktop"), //
    LBE("lbe", "application/vnd.llamagraphics.life-balance.exchange+xml"), //
    LES("les", "application/vnd.hhe.lesson-player"), //
    LHA("lha", "application/octet-stream"), //
    LINK66("link66", "application/vnd.route66.link66+xml"), //
    LIST3820("list3820", "application/vnd.ibm.modcap"), //
    LISTAFP("listafp", "application/vnd.ibm.modcap"), //
    LIST("list", "text/plain"), //
    LOG("log", "text/plain"), //
    LOSTXML("lostxml", "application/lost+xml"), //
    LRF("lrf", "application/octet-stream"), //
    LRM("lrm", "application/vnd.ms-lrm"), //
    LTF("ltf", "application/vnd.frogans.ltf"), //
    LVP("lvp", "audio/vnd.lucent.voice"), //
    LWP("lwp", "application/vnd.lotus-wordpro"), //
    LZH("lzh", "application/octet-stream"), //
    M13("m13", "application/x-msmediaview"), //
    M14("m14", "application/x-msmediaview"), //
    M1V("m1v", "video/mpeg"), //
    M21("m21", "application/mp21"), //
    M2A("m2a", "audio/mpeg"), //
    M2V("m2v", "video/mpeg"), //
    M3A("m3a", "audio/mpeg"), //
    M3U8("m3u8", "application/vnd.apple.mpegurl"), //
    M3U("m3u", "audio/x-mpegurl"), //
    M4A("m4a", "audio/mp4"), //
    M4B("m4b", "audio/mp4"), //
    M4R("m4r", "audio/mp4"), //
    M4U("m4u", "video/vnd.mpegurl"), //
    M4V("m4v", "video/mp4"), //
    MA("ma", "application/mathematica"), //
    MAC("mac", "image/x-macpaint"), //
    MADS("mads", "application/mads+xml"), //
    MAG("mag", "application/vnd.ecowin.chart"), //
    MAKER("maker", "application/vnd.framemaker"), //
    MAN("man", "text/troff"), //
    MATHML("mathml", "application/mathml+xml"), //
    MB("mb", "application/mathematica"), //
    MBK("mbk", "application/vnd.mobius.mbk"), //
    MBOX("mbox", "application/mbox"), //
    MC1("mc1", "application/vnd.medcalcdata"), //
    MCD("mcd", "application/vnd.mcd"), //
    MCURL("mcurl", "text/vnd.curl.mcurl"), //
    MDB("mdb", "application/x-msaccess"), //
    MDI("mdi", "image/vnd.ms-modi"), //
    MESH("mesh", "model/mesh"), //
    META4("meta4", "application/metalink4+xml"), //
    ME("me", "text/troff"), //
    METS("mets", "application/mets+xml"), //
    MFM("mfm", "application/vnd.mfmp"), //
    MFT("mft", "application/rpki-manifest"), //
    MGP("mgp", "application/vnd.osgeo.mapguide.package"), //
    MGZ("mgz", "application/vnd.proteus.magazine"), //
    MHT("mht", "message/rfc822"), //
    MHTML("mhtml", "message/rfc822"), //
    MID("mid", "audio/midi"), //
    MIDI("midi", "audio/midi"), //
    MIF("mif", "application/vnd.mif"), //
    MIME("mime", "message/rfc822"), //
    MJ2("mj2", "video/mj2"), //
    MJP2("mjp2", "video/mj2"), //
    MLP("mlp", "application/vnd.dolby.mlp"), //
    MMD("mmd", "application/vnd.chipnuts.karaoke-mmd"), //
    MMF("mmf", "application/vnd.smaf"), //
    MMR("mmr", "image/vnd.fujixerox.edmics-mmr"), //
    MNY("mny", "application/x-msmoney"), //
    MOBI("mobi", "application/x-mobipocket-ebook"), //
    MODS("mods", "application/mods+xml"), //
    MOVIE("movie", "video/x-sgi-movie"), //
    MOV("mov", "video/quicktime"), //
    MP1("mp1", "audio/mpeg"), //
    MP21("mp21", "application/mp21"), //
    MP2A("mp2a", "audio/mpeg"), //
    MP2("mp2", "audio/mpeg"), //
    MP3("mp3", "audio/mpeg"), //
    MP4A("mp4a", "audio/mp4"), //
    MP4S("mp4s", "application/mp4"), //
    MP4("mp4", "video/mp4"), //
    MP4V("mp4v", "video/mp4"), //
    MPA("mpa", "audio/mpeg"), //
    MPC("mpc", "application/vnd.mophun.certificate"), //
    MPEGA("mpega", "audio/x-mpeg"), //
    MPEG("mpeg", "video/mpeg"), //
    MPE("mpe", "video/mpeg"), //
    MPG4("mpg4", "video/mp4"), //
    MPGA("mpga", "audio/mpeg"), //
    MPG("mpg", "video/mpeg"), //
    MPKG("mpkg", "application/vnd.apple.installer+xml"), //
    MPM("mpm", "application/vnd.blueice.multipass"), //
    MPN("mpn", "application/vnd.mophun.application"), //
    MPP("mpp", "application/vnd.ms-project"), //
    MPT("mpt", "application/vnd.ms-project"), //
    MPV2("mpv2", "video/mpeg2"), //
    MPY("mpy", "application/vnd.ibm.minipay"), //
    MQY("mqy", "application/vnd.mobius.mqy"), //
    MRC("mrc", "application/marc"), //
    MRCX("mrcx", "application/marcxml+xml"), //
    MSCML("mscml", "application/mediaservercontrol+xml"), //
    MSEED("mseed", "application/vnd.fdsn.mseed"), //
    MSEQ("mseq", "application/vnd.mseq"), //
    MSF("msf", "application/vnd.epson.msf"), //
    MSH("msh", "model/mesh"), //
    MSI("msi", "application/x-msdownload"), //
    MSL("msl", "application/vnd.mobius.msl"), //
    MS("ms", "text/troff"), //
    MSTY("msty", "application/vnd.muvee.style"), //
    MTS("mts", "model/vnd.mts"), //
    MUS("mus", "application/vnd.musician"), //
    MUSICXML("musicxml", "application/vnd.recordare.musicxml+xml"), //
    MVB("mvb", "application/x-msmediaview"), //
    MWF("mwf", "application/vnd.mfer"), //
    MXF("mxf", "application/mxf"), //
    MXL("mxl", "application/vnd.recordare.musicxml"), //
    MXML("mxml", "application/xv+xml"), //
    MXS("mxs", "application/vnd.triscape.mxs"), //
    MXU("mxu", "video/vnd.mpegurl"), //
    N3("n3", "text/n3"), //
    NB("nb", "application/mathematica"), //
    NBP("nbp", "application/vnd.wolfram.player"), //
    NC("nc", "application/x-netcdf"), //
    NCX("ncx", "application/x-dtbncx+xml"), //
    N_GAGE("n-gage", "application/vnd.nokia.n-gage.symbian.install"), //
    NGDAT("ngdat", "application/vnd.nokia.n-gage.data"), //
    NLU("nlu", "application/vnd.neurolanguage.nlu"), //
    NML("nml", "application/vnd.enliven"), //
    NND("nnd", "application/vnd.noblenet-directory"), //
    NNS("nns", "application/vnd.noblenet-sealer"), //
    NNW("nnw", "application/vnd.noblenet-web"), //
    NPX("npx", "image/vnd.net-fpx"), //
    NSF("nsf", "application/vnd.lotus-notes"), //
    NWS("nws", "message/rfc822"), //
    OA2("oa2", "application/vnd.fujitsu.oasys2"), //
    OA3("oa3", "application/vnd.fujitsu.oasys3"), //
    O("o", "application/octet-stream"), //
    OAS("oas", "application/vnd.fujitsu.oasys"), //
    OBD("obd", "application/x-msbinder"), //
    OBJ("obj", "application/octet-stream"), //
    ODA("oda", "application/oda"), //
    ODB("odb", "application/vnd.oasis.opendocument.database"), //
    ODC("odc", "application/vnd.oasis.opendocument.chart"), //
    ODF("odf", "application/vnd.oasis.opendocument.formula"), //
    ODFT("odft", "application/vnd.oasis.opendocument.formula-template"), //
    ODG("odg", "application/vnd.oasis.opendocument.graphics"), //
    ODI("odi", "application/vnd.oasis.opendocument.image"), //
    ODM("odm", "application/vnd.oasis.opendocument.text-master"), //
    ODP("odp", "application/vnd.oasis.opendocument.presentation"), //
    ODS("ods", "application/vnd.oasis.opendocument.spreadsheet"), //
    ODT("odt", "application/vnd.oasis.opendocument.text"), //
    OGA("oga", "audio/ogg"), //
    OGG("ogg", "audio/ogg"), //
    OGV("ogv", "video/ogg"), //
    OGX("ogx", "application/ogg"), //
    ONEPKG("onepkg", "application/onenote"), //
    ONETMP("onetmp", "application/onenote"), //
    ONETOC2("onetoc2", "application/onenote"), //
    ONETOC("onetoc", "application/onenote"), //
    OPF("opf", "application/oebps-package+xml"), //
    OPRC("oprc", "application/vnd.palm"), //
    ORG("org", "application/vnd.lotus-organizer"), //
    OSF("osf", "application/vnd.yamaha.openscoreformat"), //
    OSFPVG("osfpvg", "application/vnd.yamaha.openscoreformat.osfpvg+xml"), //
    OTC("otc", "application/vnd.oasis.opendocument.chart-template"), //
    OTF("otf", "application/x-font-otf"), //
    OTG("otg", "application/vnd.oasis.opendocument.graphics-template"), //
    OTH("oth", "application/vnd.oasis.opendocument.text-web"), //
    OTI("oti", "application/vnd.oasis.opendocument.image-template"), //
    OTM("otm", "application/vnd.oasis.opendocument.text-master"), //
    OTP("otp", "application/vnd.oasis.opendocument.presentation-template"), //
    OTS("ots", "application/vnd.oasis.opendocument.spreadsheet-template"), //
    OTT("ott", "application/vnd.oasis.opendocument.text-template"), //
    OXPS("oxps", "application/oxps"), //
    OXT("oxt", "application/vnd.openofficeorg.extension"), //
    P10("p10", "application/pkcs10"), //
    P12("p12", "application/x-pkcs12"), //
    P7B("p7b", "application/x-pkcs7-certificates"), //
    P7C("p7c", "application/pkcs7-mime"), //
    P7M("p7m", "application/pkcs7-mime"), //
    P7R("p7r", "application/x-pkcs7-certreqresp"), //
    P7S("p7s", "application/pkcs7-signature"), //
    P8("p8", "application/pkcs8"), //
    PAS("pas", "text/x-pascal"), //
    PAW("paw", "application/vnd.pawaafile"), //
    PBD("pbd", "application/vnd.powerbuilder6"), //
    PBM("pbm", "image/x-portable-bitmap"), //
    PCAP("pcap", "application/vnd.tcpdump.pcap"), //
    PCF("pcf", "application/x-font-pcf"), //
    PCL("pcl", "application/vnd.hp-pcl"), //
    PCLXL("pclxl", "application/vnd.hp-pclxl"), //
    PCT("pct", "image/pict"), //
    PCURL("pcurl", "application/vnd.curl.pcurl"), //
    PCX("pcx", "image/x-pcx"), //
    PDB("pdb", "application/vnd.palm"), //
    PDF("pdf", "application/pdf"), //
    PFA("pfa", "application/x-font-type1"), //
    PFB("pfb", "application/x-font-type1"), //
    PFM("pfm", "application/x-font-type1"), //
    PFR("pfr", "application/font-tdpfr"), //
    PFX("pfx", "application/x-pkcs12"), //
    PGM("pgm", "image/x-portable-graymap"), //
    PGN("pgn", "application/x-chess-pgn"), //
    PGP("pgp", "application/pgp-encrypted"), //
    PIC("pic", "image/pict"), //
    PICT("pict", "image/pict"), //
    PKG("pkg", "application/octet-stream"), //
    PKI("pki", "application/pkixcmp"), //
    PKIPATH("pkipath", "application/pkix-pkipath"), //
    PLB("plb", "application/vnd.3gpp.pic-bw-large"), //
    PLC("plc", "application/vnd.mobius.plc"), //
    PLF("plf", "application/vnd.pocketlearn"), //
    PLIST("plist", "text/xml"), //
    PLS("pls", "application/pls+xml"), //
    PL("pl", "text/plain"), //
    PML("pml", "application/vnd.ctc-posml"), //
    PNG("png", "image/png"), //
    PNM("pnm", "image/x-portable-anymap"), //
    PNT("pnt", "image/x-macpaint"), //
    PORTPKG("portpkg", "application/vnd.macports.portpkg"), //
    POT("pot", "application/vnd.ms-powerpoint"), //
    POTM("potm", "application/vnd.ms-powerpoint.template.macroenabled.12"), //
    POTX("potx", "application/vnd.openxmlformats-officedocument.presentationml.template"), //
    PPA("ppa", "application/vnd.ms-powerpoint"), //
    PPAM("ppam", "application/vnd.ms-powerpoint.addin.macroenabled.12"), //
    PPD("ppd", "application/vnd.cups-ppd"), //
    PPM("ppm", "image/x-portable-pixmap"), //
    PPS("pps", "application/vnd.ms-powerpoint"), //
    PPSM("ppsm", "application/vnd.ms-powerpoint.slideshow.macroenabled.12"), //
    PPSX("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow"), //
    PPT("ppt", "application/vnd.ms-powerpoint"), //
    PPTM("pptm", "application/vnd.ms-powerpoint.presentation.macroenabled.12"), //
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"), //
    PQA("pqa", "application/vnd.palm"), //
    PRC("prc", "application/x-mobipocket-ebook"), //
    PRE("pre", "application/vnd.lotus-freelance"), //
    PRF("prf", "application/pics-rules"), //
    PS("ps", "application/postscript"), //
    PSB("psb", "application/vnd.3gpp.pic-bw-small"), //
    PSD("psd", "image/vnd.adobe.photoshop"), //
    PSF("psf", "application/x-font-linux-psf"), //
    PSKCXML("pskcxml", "application/pskc+xml"), //
    P("p", "text/x-pascal"), //
    PTID("ptid", "application/vnd.pvi.ptid1"), //
    PUB("pub", "application/x-mspublisher"), //
    PVB("pvb", "application/vnd.3gpp.pic-bw-var"), //
    PWN("pwn", "application/vnd.3m.post-it-notes"), //
    PWZ("pwz", "application/vnd.ms-powerpoint"), //
    PYA("pya", "audio/vnd.ms-playready.media.pya"), //
    PYC("pyc", "application/x-python-code"), //
    PYO("pyo", "application/x-python-code"), //
    PY("py", "text/x-python"), //
    PYV("pyv", "video/vnd.ms-playready.media.pyv"), //
    QAM("qam", "application/vnd.epson.quickanime"), //
    QBO("qbo", "application/vnd.intu.qbo"), //
    QFX("qfx", "application/vnd.intu.qfx"), //
    QPS("qps", "application/vnd.publishare-delta-tree"), //
    QTIF("qtif", "image/x-quicktime"), //
    QTI("qti", "image/x-quicktime"), //
    QT("qt", "video/quicktime"), //
    QWD("qwd", "application/vnd.quark.quarkxpress"), //
    QWT("qwt", "application/vnd.quark.quarkxpress"), //
    QXB("qxb", "application/vnd.quark.quarkxpress"), //
    QXD("qxd", "application/vnd.quark.quarkxpress"), //
    QXL("qxl", "application/vnd.quark.quarkxpress"), //
    QXT("qxt", "application/vnd.quark.quarkxpress"), //
    RA("ra", "audio/x-pn-realaudio"), //
    RAM("ram", "audio/x-pn-realaudio"), //
    RAR("rar", "application/x-rar-compressed"), //
    RAS("ras", "image/x-cmu-raster"), //
    RCPROFILE("rcprofile", "application/vnd.ipunplugged.rcprofile"), //
    RDF("rdf", "application/rdf+xml"), //
    RDZ("rdz", "application/vnd.data-vision.rdz"), //
    REP("rep", "application/vnd.businessobjects"), //
    RES("res", "application/x-dtbresource+xml"), //
    RGB("rgb", "image/x-rgb"), //
    RIF("rif", "application/reginfo+xml"), //
    RIP("rip", "audio/vnd.rip"), //
    RL("rl", "application/resource-lists+xml"), //
    RLC("rlc", "image/vnd.fujixerox.edmics-rlc"), //
    RLD("rld", "application/resource-lists-diff+xml"), //
    RM("rm", "application/vnd.rn-realmedia"), //
    RMI("rmi", "audio/midi"), //
    RMP("rmp", "audio/x-pn-realaudio-plugin"), //
    RMS("rms", "application/vnd.jcp.javame.midlet-rms"), //
    RNC("rnc", "application/relax-ng-compact-syntax"), //
    ROA("roa", "application/rpki-roa"), //
    ROFF("roff", "text/troff"), //
    RP9("rp9", "application/vnd.cloanto.rp9"), //
    RPM("rpm", "application/x-rpm"), //
    RPSS("rpss", "application/vnd.nokia.radio-presets"), //
    RPST("rpst", "application/vnd.nokia.radio-preset"), //
    RQ("rq", "application/sparql-query"), //
    RS("rs", "application/rls-services+xml"), //
    RSD("rsd", "application/rsd+xml"), //
    RSS("rss", "application/rss+xml"), //
    RTF("rtf", "application/rtf"), //
    RTX("rtx", "text/richtext"), //
    SAF("saf", "application/vnd.yamaha.smaf-audio"), //
    SBML("sbml", "application/sbml+xml"), //
    SC("sc", "application/vnd.ibm.secure-container"), //
    SCD("scd", "application/x-msschedule"), //
    SCM("scm", "application/vnd.lotus-screencam"), //
    SCQ("scq", "application/scvp-cv-request"), //
    SCS("scs", "application/scvp-cv-response"), //
    SCURL("scurl", "text/vnd.curl.scurl"), //
    SDA("sda", "application/vnd.stardivision.draw"), //
    SDC("sdc", "application/vnd.stardivision.calc"), //
    SDD("sdd", "application/vnd.stardivision.impress"), //
    SDKD("sdkd", "application/vnd.solent.sdkm+xml"), //
    SDKM("sdkm", "application/vnd.solent.sdkm+xml"), //
    SDP("sdp", "application/sdp"), //
    SDW("sdw", "application/vnd.stardivision.writer"), //
    SEE("see", "application/vnd.seemail"), //
    SEED("seed", "application/vnd.fdsn.seed"), //
    SEMA("sema", "application/vnd.sema"), //
    SEMD("semd", "application/vnd.semd"), //
    SEMF("semf", "application/vnd.semf"), //
    SER("ser", "application/java-serialized-object"), //
    SETPAY("setpay", "application/set-payment-initiation"), //
    SETREG("setreg", "application/set-registration-initiation"), //
    SFD_HDSTX("sfd-hdstx", "application/vnd.hydrostatix.sof-data"), //
    SFS("sfs", "application/vnd.spotfire.sfs"), //
    SGL("sgl", "application/vnd.stardivision.writer-global"), //
    SGML("sgml", "text/sgml"), //
    SGM("sgm", "text/sgml"), //
    SH("sh", "application/x-sh"), //
    SHAR("shar", "application/x-shar"), //
    SHF("shf", "application/shf+xml"), //
    SIC("sic", "application/vnd.wap.sic"), //
    SIG("sig", "application/pgp-signature"), //
    SILO("silo", "model/mesh"), //
    SIS("sis", "application/vnd.symbian.install"), //
    SISX("sisx", "application/vnd.symbian.install"), //
    SIT("sit", "application/x-stuffit"), //
    SI("si", "text/vnd.wap.si"), //
    SITX("sitx", "application/x-stuffitx"), //
    SKD("skd", "application/vnd.koan"), //
    SKM("skm", "application/vnd.koan"), //
    SKP("skp", "application/vnd.koan"), //
    SKT("skt", "application/vnd.koan"), //
    SLC("slc", "application/vnd.wap.slc"), //
    SLDM("sldm", "application/vnd.ms-powerpoint.slide.macroenabled.12"), //
    SLDX("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide"), //
    SLT("slt", "application/vnd.epson.salt"), //
    SL("sl", "text/vnd.wap.sl"), //
    SM("sm", "application/vnd.stepmania.stepchart"), //
    SMF("smf", "application/vnd.stardivision.math"), //
    SMI("smi", "application/smil+xml"), //
    SMIL("smil", "application/smil+xml"), //
    SMZIP("smzip", "application/vnd.stepmania.package"), //
    SND("snd", "audio/basic"), //
    SNF("snf", "application/x-font-snf"), //
    SO("so", "application/octet-stream"), //
    SPC("spc", "application/x-pkcs7-certificates"), //
    SPF("spf", "application/vnd.yamaha.smaf-phrase"), //
    SPL("spl", "application/x-futuresplash"), //
    SPOT("spot", "text/vnd.in3d.spot"), //
    SPP("spp", "application/scvp-vp-response"), //
    SPQ("spq", "application/scvp-vp-request"), //
    SPX("spx", "audio/ogg"), //
    SRC("src", "application/x-wais-source"), //
    SRU("sru", "application/sru+xml"), //
    SRX("srx", "application/sparql-results+xml"), //
    SSE("sse", "application/vnd.kodak-descriptor"), //
    SSF("ssf", "application/vnd.epson.ssf"), //
    SSML("ssml", "application/ssml+xml"), //
    ST("st", "application/vnd.sailingtracker.track"), //
    STC("stc", "application/vnd.sun.xml.calc.template"), //
    STD("std", "application/vnd.sun.xml.draw.template"), //
    S("s", "text/x-asm"), //
    STF("stf", "application/vnd.wt.stf"), //
    STI("sti", "application/vnd.sun.xml.impress.template"), //
    STK("stk", "application/hyperstudio"), //
    STL("stl", "application/vnd.ms-pki.stl"), //
    STR("str", "application/vnd.pg.format"), //
    STW("stw", "application/vnd.sun.xml.writer.template"), //
    SUB("sub", "text/vnd.dvb.subtitle"), //
    SUS("sus", "application/vnd.sus-calendar"), //
    SUSP("susp", "application/vnd.sus-calendar"), //
    SV4CPIO("sv4cpio", "application/x-sv4cpio"), //
    SV4CRC("sv4crc", "application/x-sv4crc"), //
    SVC("svc", "application/vnd.dvb.service"), //
    SVD("svd", "application/vnd.svd"), //
    SVG("svg", "image/svg+xml"), //
    SVGZ("svgz", "image/svg+xml"), //
    SWA("swa", "application/x-director"), //
    SWF("swf", "application/x-shockwave-flash"), //
    SWI("swi", "application/vnd.arastra.swi"), //
    SXC("sxc", "application/vnd.sun.xml.calc"), //
    SXD("sxd", "application/vnd.sun.xml.draw"), //
    SXG("sxg", "application/vnd.sun.xml.writer.global"), //
    SXI("sxi", "application/vnd.sun.xml.impress"), //
    SXM("sxm", "application/vnd.sun.xml.math"), //
    SXW("sxw", "application/vnd.sun.xml.writer"), //
    TAGLET("taglet", "application/vnd.mynfc"), //
    TAO("tao", "application/vnd.tao.intent-module-archive"), //
    TAR("tar", "application/x-tar"), //
    TCAP("tcap", "application/vnd.3gpp2.tcap"), //
    TCL("tcl", "application/x-tcl"), //
    TEACHER("teacher", "application/vnd.smart.teacher"), //
    TEI("tei", "application/tei+xml"), //
    TEICORPUS("teicorpus", "application/tei+xml"), //
    TEX("tex", "application/x-tex"), //
    TEXI("texi", "application/x-texinfo"), //
    TEXINFO("texinfo", "application/x-texinfo"), //
    TEXT("text", "text/plain"), //
    TFI("tfi", "application/thraud+xml"), //
    TFM("tfm", "application/x-tex-tfm"), //
    TGZ("tgz", "application/x-gzip"), //
    THMX("thmx", "application/vnd.ms-officetheme"), //
    TIFF("tiff", "image/tiff"), //
    TIF("tif", "image/tiff"), //
    TMO("tmo", "application/vnd.tmobile-livetv"), //
    TORRENT("torrent", "application/x-bittorrent"), //
    TPL("tpl", "application/vnd.groove-tool-template"), //
    TPT("tpt", "application/vnd.trid.tpt"), //
    TRA("tra", "application/vnd.trueapp"), //
    TRM("trm", "application/x-msterminal"), //
    TR("tr", "text/troff"), //
    TSD("tsd", "application/timestamped-data"), //
    TSV("tsv", "text/tab-separated-values"), //
    TTC("ttc", "application/x-font-ttf"), //
    T("t", "text/troff"), //
    TTF("ttf", "application/x-font-ttf"), //
    TTL("ttl", "text/turtle"), //
    TWD("twd", "application/vnd.simtech-mindmapper"), //
    TWDS("twds", "application/vnd.simtech-mindmapper"), //
    TXD("txd", "application/vnd.genomatix.tuxedo"), //
    TXF("txf", "application/vnd.mobius.txf"), //
    TXT("txt", "text/plain"), //
    U32("u32", "application/x-authorware-bin"), //
    UDEB("udeb", "application/x-debian-package"), //
    UFD("ufd", "application/vnd.ufdl"), //
    UFDL("ufdl", "application/vnd.ufdl"), //
    ULW("ulw", "audio/basic"), //
    UMJ("umj", "application/vnd.umajin"), //
    UNITYWEB("unityweb", "application/vnd.unity"), //
    UOML("uoml", "application/vnd.uoml+xml"), //
    URIS("uris", "text/uri-list"), //
    URI("uri", "text/uri-list"), //
    URLS("urls", "text/uri-list"), //
    USTAR("ustar", "application/x-ustar"), //
    UTZ("utz", "application/vnd.uiq.theme"), //
    UU("uu", "text/x-uuencode"), //
    UVA("uva", "audio/vnd.dece.audio"), //
    UVD("uvd", "application/vnd.dece.data"), //
    UVF("uvf", "application/vnd.dece.data"), //
    UVG("uvg", "image/vnd.dece.graphic"), //
    UVH("uvh", "video/vnd.dece.hd"), //
    UVI("uvi", "image/vnd.dece.graphic"), //
    UVM("uvm", "video/vnd.dece.mobile"), //
    UVP("uvp", "video/vnd.dece.pd"), //
    UVS("uvs", "video/vnd.dece.sd"), //
    UVT("uvt", "application/vnd.dece.ttml+xml"), //
    UVU("uvu", "video/vnd.uvvu.mp4"), //
    UVVA("uvva", "audio/vnd.dece.audio"), //
    UVVD("uvvd", "application/vnd.dece.data"), //
    UVVF("uvvf", "application/vnd.dece.data"), //
    UVVG("uvvg", "image/vnd.dece.graphic"), //
    UVVH("uvvh", "video/vnd.dece.hd"), //
    UVVI("uvvi", "image/vnd.dece.graphic"), //
    UVVM("uvvm", "video/vnd.dece.mobile"), //
    UVVP("uvvp", "video/vnd.dece.pd"), //
    UVVS("uvvs", "video/vnd.dece.sd"), //
    UVVT("uvvt", "application/vnd.dece.ttml+xml"), //
    UVVU("uvvu", "video/vnd.uvvu.mp4"), //
    UVV("uvv", "video/vnd.dece.video"), //
    UVVV("uvvv", "video/vnd.dece.video"), //
    UVVX("uvvx", "application/vnd.dece.unspecified"), //
    UVVZ("uvvz", "application/vnd.dece.zip"), //
    UVX("uvx", "application/vnd.dece.unspecified"), //
    UVZ("uvz", "application/vnd.dece.zip"), //
    VCARD("vcard", "text/vcard"), //
    VCD("vcd", "application/x-cdlink"), //
    VCF("vcf", "text/x-vcard"), //
    VCG("vcg", "application/vnd.groove-vcard"), //
    VCS("vcs", "text/x-vcalendar"), //
    VCX("vcx", "application/vnd.vcx"), //
    VIS("vis", "application/vnd.visionary"), //
    VIV("viv", "video/vnd.vivo"), //
    VOR("vor", "application/vnd.stardivision.writer"), //
    VOX("vox", "application/x-authorware-bin"), //
    VRML("vrml", "model/vrml"), //
    VSD("vsd", "application/vnd.visio"), //
    VSF("vsf", "application/vnd.vsf"), //
    VSS("vss", "application/vnd.visio"), //
    VST("vst", "application/vnd.visio"), //
    VSW("vsw", "application/vnd.visio"), //
    VTU("vtu", "model/vnd.vtu"), //
    VXML("vxml", "application/voicexml+xml"), //
    W3D("w3d", "application/x-director"), //
    WAD("wad", "application/x-doom"), //
    WAR("war", "application/octet-stream"), //
    WAV("wav", "audio/x-wav"), //
    WAX("wax", "audio/x-ms-wax"), //
    WBMP("wbmp", "image/vnd.wap.wbmp"), //
    WBS("wbs", "application/vnd.criticaltools.wbs+xml"), //
    WBXML("wbxml", "application/vnd.wap.wbxml"), //
    WCM("wcm", "application/vnd.ms-works"), //
    WDB("wdb", "application/vnd.ms-works"), //
    WEBA("weba", "audio/webm"), //
    WEBM("webm", "video/webm"), //
    WEBP("webp", "image/webp"), //
    WG("wg", "application/vnd.pmi.widget"), //
    WGT("wgt", "application/widget"), //
    WIZ("wiz", "application/msword"), //
    WKS("wks", "application/vnd.ms-works"), //
    WMA("wma", "audio/x-ms-wma"), //
    WMD("wmd", "application/x-ms-wmd"), //
    WMF("wmf", "application/x-msmetafile"), //
    WMLC("wmlc", "application/vnd.wap.wmlc"), //
    WMLSC("wmlsc", "application/vnd.wap.wmlscriptc"), //
    WMLS("wmls", "text/vnd.wap.wmlscript"), //
    WML("wml", "text/vnd.wap.wml"), //
    WM("wm", "video/x-ms-wm"), //
    WMV("wmv", "video/x-ms-wmv"), //
    WMX("wmx", "video/x-ms-wmx"), //
    WMZ("wmz", "application/x-ms-wmz"), //
    WOFF("woff", "application/x-font-woff"), //
    WPD("wpd", "application/vnd.wordperfect"), //
    WPL("wpl", "application/vnd.ms-wpl"), //
    WPS("wps", "application/vnd.ms-works"), //
    WQD("wqd", "application/vnd.wqd"), //
    WRI("wri", "application/x-mswrite"), //
    WRL("wrl", "model/vrml"), //
    WSDL("wsdl", "application/wsdl+xml"), //
    WSPOLICY("wspolicy", "application/wspolicy+xml"), //
    WTB("wtb", "application/vnd.webturbo"), //
    WVX("wvx", "video/x-ms-wvx"), //
    X32("x32", "application/x-authorware-bin"), //
    X3D("x3d", "application/vnd.hzn-3d-crossword"), //
    XAP("xap", "application/x-silverlight-app"), //
    XAR("xar", "application/vnd.xara"), //
    XBAP("xbap", "application/x-ms-xbap"), //
    XBD("xbd", "application/vnd.fujixerox.docuworks.binder"), //
    XBM("xbm", "image/x-xbitmap"), //
    XDF("xdf", "application/xcap-diff+xml"), //
    XDM("xdm", "application/vnd.syncml.dm+xml"), //
    XDP("xdp", "application/vnd.adobe.xdp+xml"), //
    XDSSC("xdssc", "application/dssc+xml"), //
    XDW("xdw", "application/vnd.fujixerox.docuworks"), //
    XENC("xenc", "application/xenc+xml"), //
    XER("xer", "application/patch-ops-error+xml"), //
    XFDF("xfdf", "application/vnd.adobe.xfdf"), //
    XFDL("xfdl", "application/vnd.xfdl"), //
    XHT("xht", "application/xhtml+xml"), //
    XHTML("xhtml", "application/xhtml+xml"), //
    XHVML("xhvml", "application/xv+xml"), //
    XIF("xif", "image/vnd.xiff"), //
    XLA("xla", "application/vnd.ms-excel"), //
    XLAM("xlam", "application/vnd.ms-excel.addin.macroenabled.12"), //
    XLB("xlb", "application/vnd.ms-excel"), //
    XLC("xlc", "application/vnd.ms-excel"), //
    XLM("xlm", "application/vnd.ms-excel"), //
    XLS("xls", "application/vnd.ms-excel"), //
    XLSB("xlsb", "application/vnd.ms-excel.sheet.binary.macroenabled.12"), //
    XLSM("xlsm", "application/vnd.ms-excel.sheet.macroenabled.12"), //
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), //
    XLT("xlt", "application/vnd.ms-excel"), //
    XLTM("xltm", "application/vnd.ms-excel.template.macroenabled.12"), //
    XLTX("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template"), //
    XLW("xlw", "application/vnd.ms-excel"), //
    XML("xml", "application/xml"), //
    XO("xo", "application/vnd.olpc-sugar"), //
    XOP("xop", "application/xop+xml"), //
    XPDL("xpdl", "application/xml"), //
    XPI("xpi", "application/x-xpinstall"), //
    XPM("xpm", "image/x-xpixmap"), //
    XPR("xpr", "application/vnd.is-xpr"), //
    XPS("xps", "application/vnd.ms-xpsdocument"), //
    XPW("xpw", "application/vnd.intercon.formnet"), //
    XPX("xpx", "application/vnd.intercon.formnet"), //
    XSL("xsl", "application/xml"), //
    XSLT("xslt", "application/xslt+xml"), //
    XSM("xsm", "application/vnd.syncml+xml"), //
    XSPF("xspf", "application/xspf+xml"), //
    XUL("xul", "application/vnd.mozilla.xul+xml"), //
    XVM("xvm", "application/xv+xml"), //
    XVML("xvml", "application/xv+xml"), //
    XWD("xwd", "image/x-xwindowdump"), //
    XYZ("xyz", "chemical/x-xyz"), //
    YANG("yang", "application/yang"), //
    YIN("yin", "application/yin+xml"), //
    Z("Zz", "application/x-compress"), //
    ZAZ("zaz", "application/vnd.zzazz.deck+xml"), //
    ZIP("zip", "application/zip"), //
    ZIR("zir", "application/vnd.zul"), //
    ZIRZ("zirz", "application/vnd.zul"), //
    ZMM("zmm", "application/vnd.handheld-entertainment+xml"), //

    WJSON("wjson", "application/json"), //
    WXML("wxml", "application/xml") //

    ;

    /**
     * Map of [Extension, Media Type]
     */
    private final static Map<String, String> extensionMap = toMap();
    
    /**
     * Extension
     */
    private final String extension;

    /**
     * Mime type of media type
     */
    private final String mediaType;

    /**
     * Build a new instance of {@link Format}.
     * 
     * @param extension
     * @param mediaType
     */
    private Format(String extension, String mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String toString() {
        return extension;
    }

    public static Map<String, String> asMap() {
    	return extensionMap;
    }
    
    /**
     * Transform {@link Format} into a {@link Map} of Extension, Media Type
     * 
     * @return a {@link Map}.
     */
    protected static Map<String, String> toMap() {
        Map<String, String> result = new HashMap<String, String>();
        for (Format format : Format.values()) {
            result.put(format.extension, format.mediaType);
        }
        return result;
    }

    /**
     * @param extension
     * @return first associated {@link Format} for specified extension} or null if none was found.
     */
    public static Format valueForExtension(String extension) {
        for (Format format : Format.values()) {
            if (format.extension.equals(extension)) {
                return format;
            }
        }
        return null;
    }

    /**
     * @param mediatype
     * @return first associated {@link Format} for specified {@link MediaType} or null if none was found.
     */
    public static Format valueForMediaType(String mediatype) {
        for (Format format : Format.values()) {
            if (format.mediaType.equals(mediatype)) {
                return format;
            }
        }
        return null;
    }

    /**
     * @param mediatype
     * @return {@link List} of associated {@link Format} for specified {@link MediaType}
     */
    public static List<Format> valuesForMediaType(String mediatype) {
        List<Format> result = new ArrayList<Format>();
        for (Format format : Format.values()) {
            if (format.mediaType.equals(mediatype)) {
                result.add(format);
            }
        }
        return result;
    }

    /**
     * @param mediatype
     * @return a {@link Set} of {@link Format} which match {@link Set} of media type.
     */
    public static Set<Format> valuesForMediaType(Set<String> mediatypes) {
        SortedSet<Format> formats = new TreeSet<>();
        for (Format format : Format.values()) {
            if (mediatypes.contains(format.mediaType)) {
                formats.add(format);
            }
        }
        return formats;
    }
}
