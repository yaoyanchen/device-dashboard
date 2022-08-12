package com.easy.dashboard.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenshinImpact {
    final static String[] threeWeapons = new String[] { "弹弓", "神射手之誓", "鸦羽弓", "翡玉法球", "讨龙英杰谭", "魔导绪论", "黑缨枪", "以理服人",

            "沐浴龙血的剑", "铁影阔剑", "飞天御剑", "黎明神剑", "冷刃" }; // 三星武器：13

    final static String[] fourWeapons = new String[] { "弓藏", "祭礼弓", "绝弦", "西风猎弓", "昭心", "祭礼残章", "流浪乐章", "西风秘典", "西风长枪",

            "匣里灭辰", "雨裁", "祭礼大剑", "钟剑", "西风大剑", "匣里龙吟", "祭礼剑", "笛剑", "西风剑" }; // 四星武器：18

    final static String[] fiveWeapons = new String[] { "弓  阿莫斯之弓", "弓  天空之翼", "法器  四风原典", "法器  天空之卷", "长枪  和璞鸢",

            "长枪  天空之脊", "双手剑  狼的末路", "双手剑  天空之傲", "单手剑  天空之刃", "单手剑  风鹰剑" };

    final static String[] fourRole = new String[] { "辛焱", "砂糖", "迪奥娜", "重云", "诺艾尔", "班尼特", "菲谢尔", "凝光", "行秋", "北斗",

            "香菱", "安柏", "雷泽", "凯亚", "芭芭拉", "丽莎", "罗莎莉亚", "早柚", "九条裟罗" }; // 四星角色：19

    final static String[] fiveRole = new String[] { "霆霓快雨：刻晴", "星天水镜：莫娜", "冻冻回魂夜：七七", "晨曦的暗面：迪卢克", "蒲公英骑士：琴" }; // 五星角色：5

    static int WX[] = new int[100], t = 0, n = 0; // 记录数据

    public static int chou = 0, x, baodi = 0, y, xiaobaodi = 0, dabaodi = 0, type = 0;

    static int sixing = 0, wuxing = 0, sum = 0;
    static List<Integer> wuxin = new ArrayList<>();
    static List<String> result = new ArrayList<>();
    public static StringBuilder menu = new StringBuilder();
    public static List<String> Lottery() {
        result = new ArrayList<>();
        menu();
        if (type == 0 && chou == 0)
            return result;

        else if (type == -1)

            query();

        else
            for (int i = 0; i < chou; i++) {
                xiaobaodi++;
                dabaodi++;
                sum++;
                Random rand = new Random();

                x = rand.nextInt(100000);

                if (xiaobaodi >= 10) {

                    baodi = rand.nextInt(2);

                    if (baodi == 0) {

                        fourRole();

                    } else if (baodi == 1) {
                        fourWeapons();
                    }

                    xiaobaodi = 0;

                    sixing++;

                } else if (dabaodi >= 90) {
                    baodi = rand.nextInt(2);
                    if (baodi == 0) {
                        fiveRole();
                    } else if (baodi == 1) {
                        fiveWeapons();
                    }

                    dabaodi = 0;

                    wuxing++;

                } else if (x < 600) {

                    baodi = rand.nextInt(2);

                    if (baodi == 0) {
                        fiveRole();
                    } else if (baodi == 1) {
                        fiveWeapons();
                    }

                    dabaodi = 0;

                    wuxing++;

                } else if (x < 5700) {
                    baodi = rand.nextInt(2);
                    if (baodi == 0) {
                        fourRole();

                    } else if (baodi == 1) {
                        fourWeapons();
                    }
                    sixing++;
                    xiaobaodi = 0;
                } else {
                    threeWeapons();
                }
            }
        return result;
    }



    static void menu() {
        menu = new StringBuilder();
        if (sum > 0) {
            menu.append("抽卡总数：").append(sum).append("    四星数量：").append(sixing).append("    五星数量：").append(wuxing);
            if (wuxing > 0) {
                int x = wuxin.stream().reduce(Integer::sum).orElse(0) / wuxin.size();
                System.out.println(wuxin);
                menu.append("   平均").append(x).append("抽一个金");
            }
        }
    }



    static void query() {
        if (t > 0) {
            System.out.println("抽中的五星为：");
            for (int m = 0; m < t; m++) {
                if (WX[m] >= 100) {
                    y = WX[m] - 100;
                    System.out.print(fiveWeapons[y] + "\t\t");
                    n++;
                } else {
                    y = WX[m] - 10;
                    System.out.print(fiveRole[y] + "\t\t");
                    n++;
                }

                if (n == 2) {
                    System.out.println();
                    n = 0;
                }

            }

        } else
            System.out.print("查询失败。");
        System.out.println();
    }



    static void threeWeapons() {
        Random rand = new Random();
        y = rand.nextInt(13);
        result.add("三星武器 *** " + threeWeapons[y]);
    }



    static void fourWeapons() {
        Random rand = new Random();
        y = rand.nextInt(18);
        result.add("四星武器  ****  " + fourWeapons[y]);
    }



    static void fiveWeapons() {
        Random rand = new Random();
        y = rand.nextInt(10);
        result.add("五星武器  *****   " + fiveWeapons[y]);
        WX[t] = y + 100;
        wuxin.add(WX[t]);
        t++;
    }



    static void fourRole() {
        Random rand = new Random();
        y = rand.nextInt(19);
        result.add("四星角色 *****  " + fourRole[y]);
    }


    static void fiveRole() {
        Random rand = new Random();
        y = rand.nextInt(5);
        result.add("五星角色  *****   " + fiveRole[y]);
        WX[t] = y + 10;
        wuxin.add(WX[t]);
        t++;
    }

    public static void main(String[] args) {
        chou = 10;
        type = 0;
        List<String> lottery = Lottery();
        System.out.println(lottery);
    }
}
