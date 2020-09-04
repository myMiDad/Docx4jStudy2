package com.zzqa.docx4j2Word;

import com.zzqa.pojo.FaultUnitInfo;
import com.zzqa.pojo.Waveform;
import com.zzqa.pojo.WaveformChild;
import com.zzqa.utils.Docx4jUtil;
import com.zzqa.utils.LoadDataUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.toc.Toc;
import org.docx4j.toc.TocGenerator;
import org.docx4j.toc.TocHelper;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: Docx4j2WordMain
 * Description:
 *
 * @author 张文豪
 * @date 2020/8/3 11:17
 */
public class Docx4j2WordMain {
    public static void main(String[] args) {
        try {
            WordprocessingMLPackage wpMLPackage = WordprocessingMLPackage.createPackage();

            //数据准备
            String reportName = "上海东滩风电场";  //项目名称
            long time = System.currentTimeMillis();  //报告时间
            String logoPath = "D:\\01_ideaspace\\Docx4jStudy\\src\\main\\resources\\images\\logo.png"; //封面logo路径
            String linePath = "src\\main\\resources\\images\\横线.png";   //封面横线路径
            String name1 = "蔡志雄";
            String name2 = "王忠建";
            String name3 = "陆殿忠";

            int normalPart = 106;   //正常机组数量
            int warningPart = 20;   //预警机组数量
            int alarmPart = 12; //报警机组数量
            //最后保存的文件名
            String fileName = reportName + getDate(new Date(time)) + "震动分析报告.docx";
            String paragraphContent = "上海东滩风电场共有机组10台。项目每台机组配置一套在线状态监测系统，用于监控传动链部件运行情况，根据CS2000在线监测系统测试数据分析，风场各机组运行总体平稳，本次分析结果显示：该风场共有X台机组处于亚健康状态，X台机组处于故障状态，主要为发电机和齿轮箱。";
            String targetFilePath = "D:/AutoExport/docx4j2";
            String targetFile = targetFilePath + "/" + fileName;
            //PageContent2的数据准备
            List<Map<String,String>> mapList = createData();
            List<List<Waveform>> chartData = createChartData();
            //PageContent5数据准备
            List<String> dataContent5 = createDataContent5();
            //PageContent6数据准备
            List<FaultUnitInfo> dataContent6 = createDataContent6();

            Cover cover = new Cover();
            //创建封面
            wpMLPackage = cover.createCover(wpMLPackage, reportName,time, logoPath, linePath,name1,name2,name3);
            //在这个前面添加目录
            Docx4jUtil.addNextSection(wpMLPackage);
            //文件内容1:项目概述
            PageContent1 pageContent1 = new PageContent1();
            pageContent1.createPageContent1(wpMLPackage, paragraphContent, normalPart, warningPart, alarmPart);
            //文件内容2:运行状况
            PageContent2 pageContent2 = new PageContent2();
            pageContent2.createPageContent2(wpMLPackage, mapList);
            //文件内容3：震动图谱
//            PageContent3 pageContent3 = new PageContent3();
//            pageContent3.createPageContent3(wpMLPackage, chartData, 0);
            //新建创建序号对象
            NumberingCreate numberingCreate = new NumberingCreate(wpMLPackage);
            //使用原序号
//            numberingCreate.unmarshlDefaultNumbering();

            //第五章节：处理建议
            PageContent5 pageContent5 = new PageContent5();
            pageContent5.createPageContent5(wpMLPackage, reportName, dataContent5,numberingCreate);
            //第六章节：故障机组详细分析
            PageContent6 pageContent6 = new PageContent6();
            pageContent6.createPageContent6(wpMLPackage, dataContent6,numberingCreate);
            //文件内容8：补充说明
            PageContent8 pageContent8 = new PageContent8();
            pageContent8.createPageContent8(wpMLPackage,numberingCreate);
            //附件
            AccessoryContent accessoryContent = new AccessoryContent();
            accessoryContent.createAccessoryContent(wpMLPackage);

            //保存文件
            File docxFile = new File(targetFilePath);
            if (!docxFile.exists() && !docxFile.isDirectory()) {
                docxFile.mkdirs();
            }

            //生成目录
            TocGenerator tocGenerator = new TocGenerator(wpMLPackage);
            Toc.setTocHeadingText("目录");
            tocGenerator.generateToc(15, TocHelper.DEFAULT_TOC_INSTRUCTION, true);

            wpMLPackage.save(new File(targetFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> createDataContent5(){
        String str1 = "01#の持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n";
        String str2 = "02#の持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n";
        String str3 = "03#の持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n";
        String str4 = "04#の持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n持续关注齿轮箱运行状况；\n停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。\n";

        List<String> list = new ArrayList<>();
        list.add(str1);
        list.add(str2);
        list.add(str3);
        list.add(str4);
        return list;
    }

    public static List<FaultUnitInfo> createDataContent6(){
        List<FaultUnitInfo> list = new ArrayList<>();

        FaultUnitInfo faultUnitInfo1 = new FaultUnitInfo();
        faultUnitInfo1.setUnitName("01#机组");
        faultUnitInfo1.setContent("齿轮箱行星轮振动有效值较大时已达到0.7g（图1-1）；\n" +
                "齿轮箱行星轮包络谱中可以看到振动在太阳轮转频（1.875Hz）处较大，且存在多次谐频（图1-2）；\n" +
                "发电机自由端轴承振动有效值较大时已达到2.8g（图1-3）；\n" +
                "发电机自由端轴承振动时域波形中存在明显的冲击（图1-4）；\n" +
                "发电机自由端轴承包络谱图中振动在轴承内环故障频率（150Hz）处较大，且存在多次谐频（图1-5）。\n");
        faultUnitInfo1.setConclusion("齿轮箱行星轮系啮合不良；\n" +
                "发电机自由端轴承严重磨损。\n");
        String[] arr1 = {"图1-1 01#机组齿轮箱行星轮振动有效值趋势图", "D:/AutoExport/docx4j2/images/1.png"};
        String[] arr2 = {"图1-2 01#机组齿轮箱行星轮振动包络谱", "D:/AutoExport/docx4j2/images/2.png"};
        String[] arr3 = {"图1-3 01#机组发电机自由端轴承径向振动有效值趋势图", "D:/AutoExport/docx4j2/images/3.png"};
        String[] arr4 = {"图1-4 01#机组发电机自由端轴承时域波形", "D:/AutoExport/docx4j2/images/4.png"};
        String[] arr5 = {"图1-5 01#机组发电机自由端轴承包络谱图", "D:/AutoExport/docx4j2/images/5.png"};
        List<String[]> list1 = new ArrayList<>();
        list1.add(arr1);
        list1.add(arr2);
        list1.add(arr3);
        list1.add(arr4);
        list1.add(arr5);
        faultUnitInfo1.setImageList(list1);
        list.add(faultUnitInfo1);


        FaultUnitInfo faultUnitInfo2 = new FaultUnitInfo();
        faultUnitInfo2.setUnitName("02#机组");
        faultUnitInfo2.setContent("齿轮箱行星轮振动有效值较大时已达到0.7g（图1-1）；\n" +
                "齿轮箱行星轮包络谱中可以看到振动在太阳轮转频（1.875Hz）处较大，且存在多次谐频（图1-2）；\n" +
                "发电机自由端轴承振动有效值较大时已达到2.8g（图1-3）；\n" +
                "发电机自由端轴承振动时域波形中存在明显的冲击（图1-4）；\n" +
                "发电机自由端轴承包络谱图中振动在轴承内环故障频率（150Hz）处较大，且存在多次谐频（图1-5）。\n");
        faultUnitInfo2.setConclusion("齿轮箱行星轮系啮合不良；\n" +
                "发电机自由端轴承严重磨损。\n");
        faultUnitInfo2.setImageList(list1);
        list.add(faultUnitInfo2);


        return list;
    }

    /**
     * 数据准备
     *
     * @return
     */
    public static List<Map<String,String>> createData() {
        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("id", "CH1");
        map.put("location", "主轴轴承");
        map.put("direction", "径向");
        map.put("type", "低频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH2");
        map.put("location", "齿轮箱输入轴");
        map.put("direction", "径向");
        map.put("type", "低频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH3");
        map.put("location", "行星齿轮");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH4");
        map.put("location", "齿轮箱高速轴前轴承");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH5");
        map.put("location", "齿轮箱输出轴");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH6");
        map.put("location", "发电机传动端");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("id", "CH7");
        map.put("location", "发电机自由端");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);

        return mapList;
    }


    public static List<List<Waveform>> createChartData(){
        Random random = new Random();
        List<List<Waveform>> list = new ArrayList<>();
        for (int j=0; j<5; j++){
            List<Waveform> list1 = new ArrayList<>();
            for (int i=0; i<6; i++){
                String machineName = "机组"+(j+1);
                String positionName = "测点"+(i+1);
                String level = random.nextInt(2)==1?"报警":"预警";
                Waveform waveform = createWaveform(machineName, positionName, level);
                list1.add(waveform);
            }
            list.add(list1);
        }
        return list;
    }

    public static Waveform createWaveform(String machineName,String positionName, String level){
        Waveform waveform = new Waveform();
        //趋势图
        String dataX1 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/趋势图X.txt");
        String[] colKeys = dataX1.split(",");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy/MM/dd HH:mm");
        long [] colKeys1 = new long[colKeys.length];    //趋势图X
        for (int i=0; i<colKeys.length; i++){
            try {
                Date parse = simpleDateFormat.parse(colKeys[i]);
                colKeys1[i] = parse.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String dataY1 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/趋势图Y.txt");
        double[][] data1 = string2DoubleArray(dataY1);  //趋势图Y
        //波形图
        String dataX2 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/波形图X.txt");
        String[] colKeys2 = dataX2.split(",");  //波形图X
        String dataY2 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/波形图Y.txt");
        double[][] data2 = string2DoubleArray(dataY2);  //波形图Y
        //频谱图
        String dataX3 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/频谱图X.txt");
        String[] colKeys3 = dataX3.split(",");  //频谱图X
        String dataY3 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/频谱图Y.txt");
        double[][] data3 = string2DoubleArray(dataY3);  //频谱图Y
        //包络图
        String dataX4 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/包络图X.txt");
        String[] colKeys4 = dataX4.split(",");  //包络图X
        String dataY4 = LoadDataUtils.ReadFile("C:/Users/Mi_dad/Desktop/包络图Y.txt");
        double[][] data4 = string2DoubleArray(dataY4);  //包络图Y


        //趋势图集合
        WaveformChild waveformChild11 = new WaveformChild();
        waveformChild11.setFeatureName("峰值");
        waveformChild11.setValue_x(colKeys1);
        waveformChild11.setValue(data1);

        WaveformChild waveformChild12 = new WaveformChild();
        waveformChild12.setFeatureName("有效值");
        waveformChild12.setValue_x(colKeys1);
        waveformChild12.setValue(data1);

        List<WaveformChild> waveformChildList = new ArrayList<>();
        waveformChildList.add(waveformChild11);
        waveformChildList.add(waveformChild12);
        //波形图
        WaveformChild waveformChild2 = new WaveformChild();
        waveformChild2.setWave_x(string2Double(colKeys2));
        waveformChild2.setValue(data2);
        //频谱图
        WaveformChild waveformChild3 = new WaveformChild();
        waveformChild3.setWave_x(string2Double(colKeys3));
        waveformChild3.setValue(data3);
        //包络图
        WaveformChild waveformChild4 = new WaveformChild();
        waveformChild4.setWave_x(string2Double(colKeys4));
        waveformChild4.setValue(data4);

        waveform.setMachineName(machineName);
        waveform.setPositionName(positionName);
        waveform.setLevel(level);
        waveform.setWave(waveformChild2);
        waveform.setSpectrum(waveformChild3);
        waveform.setSpm(waveformChild4);
        waveform.setList(waveformChildList);

        return waveform;
    }
    /**
     * 将数据转换为double数组
     *
     * @param dataY
     * @return
     */
    private static double[][] string2DoubleArray(String dataY) {
        String[] split = dataY.split(",");
        double[] dataTemp = new double[split.length];
        for (int i = 0; i < split.length; i++) {
            if (split[i] != null) {
                dataTemp[i] = Double.parseDouble(split[i]);
            }
        }
        double[][] data = {dataTemp};
        return data;
    }

    /**
     * String类型转换为Double类型
     * @param colKeys
     * @return
     */
    public static double[] string2Double(String[] colKeys){
        double [] colKeys1 = new double[colKeys.length];
        for (int i=0; i<colKeys.length; i++){
            colKeys1[i] = Double.parseDouble(colKeys[i]);
        }
        return colKeys1;
    }

    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy年M月d日");
        return sdf.format(date);
    }
}
