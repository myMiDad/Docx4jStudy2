package com.zzqa.docx4j2Word;

import com.zzqa.utils.Docx4jUtil;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.toc.Toc;
import org.docx4j.toc.TocGenerator;
import org.docx4j.toc.TocHelper;

import java.io.File;
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
            String logoPath = "F:\\01_ideaspace\\Docx4jStudy\\src\\main\\resources\\images\\logo.png"; //封面logo路径
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
            String targetFilePath = "F:/AutoExport/docx4j2";
            String targetFile = targetFilePath + "/" + fileName;
            //PageContent2的数据准备
            List<Map<String,String>> mapList = createData();
            //PageContent3数据准备
            List<Map<String, String>> dataContent3 = createDataContent3();
            Map<String, String[]> titleDataContent3 = createTitleDataContent3();
            //Pagecontent4数据准备
            Map<String, String[]> titleDataContent4 = createTitleDataContent4();
            List<Map<String, String>> dataContent4 = createDataContent4();
            //PageContent5数据准备
            Map<String, String[]> dataContent5 = createDataContent5();
            //PageContent6数据准备
            List<Map<String,Object>> dataContent6 = createDataContent6();
            //Pagecontent7数据准备
            Map<String, String[]> titleDataContent71 = createTitleDataContent71();
            List<Map<String, String>> dataContent71 = createDataContent71();
            Map<String, String[]> titleDataContent72 = createTitleDataContent72();
            List<Map<String, String>> dataContent72 = createDataContent72();
            Map<String, String[]> titleDataContent73 = createTitleDataContent73();
            List<Map<String, String>> dataContent73 = createDataContent73();

            Cover cover = new Cover();
            //创建封面
            wpMLPackage = cover.createCover(wpMLPackage, reportName,time, logoPath, linePath,name1,name2,name3);
            //在这个前面添加目录
            Docx4jUtil.addNextSection(wpMLPackage);
            //开始创建文件内容
            PageContent pageContent = new PageContent();
            //文件内容1:项目概述
            PageContent.PageContent1 pageContent1 = pageContent.new PageContent1();
            pageContent1.createPageContent(wpMLPackage, paragraphContent, normalPart, warningPart, alarmPart);
            //文件内容2:运行状况
            PageContent.PageContent2 pageContent2 = pageContent.new PageContent2();
            pageContent2.createPageContent(wpMLPackage, mapList);
            //第三章节：评估标准
            PageContent.PageContent3 pageContent3 = pageContent.new PageContent3();
            pageContent3.createPageContent(wpMLPackage,titleDataContent3,dataContent3);
            //新建创建序号对象
            NumberingCreate numberingCreate = new NumberingCreate(wpMLPackage);
            //使用原序号
//            numberingCreate.unmarshlDefaultNumbering();

            //第四章节：
            PageContent.PageContent4 pageContent4= pageContent.new PageContent4();
            pageContent4.createPageContent(wpMLPackage,titleDataContent4,dataContent4);
            //第五章节：处理建议
            PageContent.PageContent5 pageContent5 = pageContent.new PageContent5();
            pageContent5.createPageContent(wpMLPackage, reportName, dataContent5,numberingCreate);
            //第六章节：故障机组详细分析
            PageContent.PageContent6 pageContent6 = pageContent.new PageContent6();
            pageContent6.createPageContent(wpMLPackage, dataContent6,numberingCreate);
            //第七章节：正常机组数据
            PageContent.PageContent7 pageContent7 = pageContent.new PageContent7();
            pageContent7.createPageContent(wpMLPackage, titleDataContent71, dataContent71, titleDataContent72, dataContent72, titleDataContent73, dataContent73);
            //文件内容8：补充说明
            PageContent.PageContent8 pageContent8 = pageContent.new PageContent8();
            pageContent8.createPageContent(wpMLPackage,numberingCreate);
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

    public static Map<String,String[]> createTitleDataContent4(){
        Map<String,String[]> map = new HashMap<>();
        String[] arr1 = {"主轴承","齿轮箱","发电机"};
        String[] arr2 = {"分析结论"};
        String[] arr3 = {"与上季度振动趋势对比"};
        map.put("部件", arr1);
        map.put("分析结论", arr2);
        map.put("与上季度振动趋势对比", arr3);
        return map;
    }

    public static List<Map<String,String>> createDataContent4(){
        List<Map<String,String>> dataList = new ArrayList<>();
        String[]arr = {"正常","注意","警告","报警","危险"};
        Random random = new Random();
        for (int i=0; i<10; i++){
            Map<String,String> map = new HashMap<>();
            map.put("机组编号", (i+1)+"#");
            map.put("主轴承", arr[random.nextInt(5)]);
            map.put("齿轮箱", arr[random.nextInt(5)]);
            map.put("发电机", arr[random.nextInt(5)]);
//            map.put("发电机2", arr[random.nextInt(5)]);
            map.put("分析结论", "1.齿轮箱行星轮系可能存在啮合不良；\n" +
                    "2.发电机自由端轴承严重磨损。\n");
            map.put("与上季度振动趋势对比", "基本不变。");
            dataList.add(map);
        }
        return dataList;
    }
    public static Map<String,String[]> createTitleDataContent73(){
        Map<String,String[]> map = new HashMap<>();
        String[] arr1 = {"风速"};
        String[] arr2 = {"转速"};
        String[] arr3 = {"功率"};
        map.put("风速(m/s)", arr1);
        map.put("转速(rpm)", arr2);
        map.put("功率(kw)", arr3);
        return map;
    }

    public static List<Map<String,String>> createDataContent73(){
        List<Map<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<10; i++){
            Map<String,String> map = new HashMap<>();
            map.put("机组编号", (i+1)+"#");
            map.put("风速", "50");
            map.put("转速", "49");
            map.put("功率", "200");
            dataList.add(map);
        }
        return dataList;
    }
    public static Map<String,String[]> createTitleDataContent72(){
        Map<String,String[]> map = new HashMap<>();
        String[] arr1 = {"主轴前轴承","主轴后轴承"};
        String[] arr2 = {"输入轴径向","输出轴径向"};
        String[] arr3 = {"传动端径向","自由端径向"};
        map.put("主轴承(℃)", arr1);
        map.put("齿轮箱油温(℃)", arr2);
        map.put("发电机(℃)", arr3);
        return map;
    }

    public static List<Map<String,String>> createDataContent72(){
        List<Map<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<10; i++){
            Map<String,String> map = new HashMap<>();
            map.put("机组编号", (i+1)+"#");
            map.put("主轴前轴承", "50");
            map.put("主轴后轴承", "50");
            map.put("输入轴径向", "50");
            map.put("输出轴径向", "");
            map.put("传动端径向", "50");
            map.put("自由端径向", "50");
            dataList.add(map);
        }
        return dataList;
    }

    public static Map<String,String[]> createTitleDataContent71(){
        Map<String,String[]> map = new HashMap<>();
        String[] arr1 = {"主轴承径向","主轴承轴向"};
        String[] arr2 = {"齿轮箱输入轴径向","齿轮箱高速轴径向8K", "齿轮箱内齿圈径向", "齿轮箱中间轴轴向", "齿轮箱高速轴径向"};
        String[] arr3 = {"发电机前轴承径向", "发电机后轴承径向", "发电机前轴承径向8K", "发电机后轴承径向8K"};
        map.put("主轴承(g)", arr1);
        map.put("齿轮箱(g)", arr2);
        map.put("发电机(g)", arr3);
        return map;
    }

    public static List<Map<String,String>> createDataContent71(){
        List<Map<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<10; i++){
            Map<String,String> map = new HashMap<>();
            map.put("机组编号", (i+1)+"#");
            map.put("主轴承径向", "0.0199242");
            map.put("主轴承轴向", "0.0218212");
            map.put("齿轮箱输入轴径向", " 0.0876583");
            map.put("齿轮箱高速轴径向8K", "0.332858");
            map.put("齿轮箱内齿圈径向", "0.098552");
            map.put("齿轮箱中间轴轴向", "0.216615");
            map.put("齿轮箱高速轴径向", "0.316997");
            map.put("发电机前轴承径向", " 0.146045");
            map.put("发电机后轴承径向", "0.100776");
            map.put("发电机前轴承径向8K", "0.716748");
            map.put("发电机后轴承径向8K", "0.806349");
            dataList.add(map);
        }
        return dataList;
    }
    public static Map<String,String[]> createTitleDataContent3(){
        Map<String,String[]> map = new HashMap<>();
        String[] arr1 = {"主轴承"};
//        String[] arr2 = {"内齿圈","中间轴","高速轴"};;
        String[] arr2 = {"内齿圈","中间轴","高速轴","齿轮箱输入轴径向","齿轮箱高速轴径向8K", "齿轮箱内齿圈径向"};
        String[] arr3 = {"发电机轴承"};
        map.put("主轴承", arr1);
        map.put("齿轮箱", arr2);
        map.put("发电机轴承", arr3);
        return map;
    }
    public static List<Map<String,String>> createDataContent3(){
        List<Map<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<10; i++){
            Map<String,String> map = new HashMap<>();
            map.put("组件", "评估加速度(g)");
            map.put("主轴承", "3.0/-1.0");
            map.put("内齿圈", "3.0/-1.0");
            map.put("中间轴", "3.0/-1.0");
            map.put("高速轴", "");
            map.put("齿轮箱输入轴径向", " 3.0/-1.0");
            map.put("齿轮箱高速轴径向8K", "3.0/-1.0");
            map.put("齿轮箱内齿圈径向", "0.098552");
            map.put("发电机轴承", "3.0/-1.0");
            map.put("发电机后轴承", "3.0/-1.0");
            dataList.add(map);
        }
        return dataList;
    }

    public static Map<String, String[]> createDataContent5(){

        Map<String,String[]> map = new HashMap<>();
        map.put("01#机组", new String[]{"1.持续关注齿轮箱运行状况1;","2.停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。"});

        map.put("02#机组", new String[]{"1.持续关注齿轮箱运行状况2;","2.停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。"});

        map.put("03#机组", new String[]{"1.持续关注齿轮箱运行状况3;","2.停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。"});

        map.put("04#机组", new String[]{"1.持续关注齿轮箱运行状况4;","2.停机检查发电机自由端轴承运行状况，如有必要，请更换轴承。"});

        return map;
    }

    public static List<Map<String,Object>> createDataContent6(){
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map1 = new HashMap<>();
        map1.put("unit_name","01#机组");
        String[] contents={"齿轮箱行星轮振动有效值较大时已达到0.7g（图1）；" ,
                "齿轮箱行星轮包络谱中可以看到振动在太阳轮转频（1.875Hz）处较大，且存在多次谐频（图2）；" ,
                "发电机自由端轴承振动有效值较大时已达到2.8g（图3）；" ,
                "发电机自由端轴承振动时域波形中存在明显的冲击（图4）；" ,
                "发电机自由端轴承包络谱图中振动在轴承内环故障频率（150Hz）处较大，且存在多次谐频（图5）。"};
        map1.put("contents",contents);
        map1.put("conclusions", new String[]{"齿轮箱行星轮系啮合不良；", "发电机自由端轴承严重磨损。"});
        String[] arr1 = {"图1-1 01#机组齿轮箱行星轮振动有效值趋势图", "F:/AutoExport/docx4j2/images/1.png"};
        String[] arr2 = {"图1-2 01#机组齿轮箱行星轮振动包络谱", "F:/AutoExport/docx4j2/images/2.png"};
        String[] arr3 = {"图1-3 01#机组发电机自由端轴承径向振动有效值趋势图", "F:/AutoExport/docx4j2/images/3.png"};
        String[] arr4 = {"图1-4 01#机组发电机自由端轴承时域波形", "F:/AutoExport/docx4j2/images/4.png"};
        String[] arr5 = {"图1-5 01#机组发电机自由端轴承包络谱图", "F:/AutoExport/docx4j2/images/5.png"};
        List<String[]> list1 = new ArrayList<>();
        list1.add(arr1);
        list1.add(arr2);
        list1.add(arr3);
        list1.add(arr4);
        list1.add(arr5);
        map1.put("imageList",list1);
        list.add(map1);


        Map<String,Object> map2 = new HashMap<>();
        map2.put("unit_name","02#机组");
        map2.put("contents",contents);
        map2.put("imageList",list1);
        list.add(map2);


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
        map.put("ch", "CH1");
        map.put("name", "主轴轴承");
        map.put("direction", "径向");
        map.put("type", "低频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH2");
        map.put("name", "齿轮箱输入轴");
        map.put("direction", "径向");
        map.put("type", "低频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH3");
        map.put("name", "行星齿轮");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH4");
        map.put("name", "齿轮箱高速轴前轴承");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH5");
        map.put("name", "齿轮箱输出轴");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH6");
        map.put("name", "发电机传动端");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);
        map = new HashMap<>();
        map.put("ch", "CH7");
        map.put("name", "发电机自由端");
        map.put("direction", "径向");
        map.put("type", "中高频加速度传感器");
        mapList.add(map);

        return mapList;
    }





    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy年M月d日");
        return sdf.format(date);
    }
}
