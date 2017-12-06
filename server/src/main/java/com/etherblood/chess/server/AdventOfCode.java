package com.etherblood.chess.server;

/**
 *
 * @author Philipp
 */
public class AdventOfCode {

    public static void main(String[] args) {
        //http://adventofcode.com/2017
        String s = "878938232157342756754254716586975125394865297349321236586574662994429894259828536842781199252169182743449435231194436368218599463391544461745472922916562414854275449983442828344463893618282425242643322822916857935242141636187859919626885791572268272442711988367762865741341467274718149255173686839265874184176985561996454253165784192929453678326937728571781212155346592432874244741816166328693958529938367575669663228335566435273484331452883175981955679335327231995452231118936393192583338222595982522833468533262224874637449624644318418748617949417939228988293391941457722641936417456243894182668197174255786445994567477582715692336249243254711653529871336129825735249667425238573952339922948214218872417858525199642194588448543565474847272984232637466664695217176358283788781843171636841215675851778984619377575696447366844854289534215286959727688419731976631323833892247438149829975856161755122857643731945913335556288817112993911694972667656914238999291831997163412548977649491227219477796124134958527843213824792685117696631512141241496451845758655276186597724748432996276498527911292531185292149948139724345841584782352214921634858734671118495424143437282979243347831258285851259579133433182387444656386679831584933397915132785411686688447731696776459621924821667112751789884987883991845818513249994767543526169463766975791464756526911587399764736557959464923353896921342944821833991457125256329564489631352268722457628514564128231487382111682976886838192412996932924373337524262135399256658638418515239876732866596731888779532573243713128238419234963195589987539467221517535272384899524386267268959484881379944796392255419838743164714275463459351741296586465213689853743856518583451849661592844879264196761867481258778393623584884535246239794178981387632311238115362178576899121425428114696158652976277392224226268242332589546757477683398264294929442592131949398261884548427951472128841328376819241955153423452531538413492577262348369581399925647624623868299468436859667152463974949436359589931136236247929554899679139746162554183855278713574244211854227829969443151478986413333429144796664423754818256172862812877688675514142265239992529776262844329188218189254491238956497568";
        System.out.println("day 1, part1:" + day1(s, 1));
        System.out.println("day 1, part2:" + day1(s, s.length() / 2));

        int[][] a = new int[][]{
            new int[]{6046, 6349, 208, 276, 4643, 1085, 1539, 4986, 7006, 5374, 252, 4751, 226, 6757, 7495, 2923},
            new int[]{1432, 1538, 1761, 1658, 104, 826, 806, 109, 939, 886, 1497, 280, 1412, 127, 1651, 156},
            new int[]{244, 1048, 133, 232, 226, 1072, 883, 1045, 1130, 252, 1038, 1022, 471, 70, 1222, 957},
            new int[]{87, 172, 93, 73, 67, 192, 249, 239, 155, 23, 189, 106, 55, 174, 181, 116},
            new int[]{5871, 204, 6466, 6437, 5716, 232, 1513, 7079, 6140, 268, 350, 6264, 6420, 3904, 272, 5565},
            new int[]{1093, 838, 90, 1447, 1224, 744, 1551, 59, 328, 1575, 1544, 1360, 71, 1583, 75, 370},
            new int[]{213, 166, 7601, 6261, 247, 210, 4809, 6201, 6690, 6816, 7776, 2522, 5618, 580, 2236, 3598},
            new int[]{92, 168, 96, 132, 196, 157, 116, 94, 253, 128, 60, 167, 192, 156, 76, 148},
            new int[]{187, 111, 141, 143, 45, 132, 140, 402, 134, 227, 342, 276, 449, 148, 170, 348},
            new int[]{1894, 1298, 1531, 1354, 1801, 974, 85, 93, 1712, 130, 1705, 110, 314, 107, 449, 350},
            new int[]{1662, 1529, 784, 1704, 1187, 83, 422, 146, 147, 1869, 1941, 110, 525, 1293, 158, 1752},
            new int[]{162, 1135, 3278, 1149, 3546, 3686, 182, 149, 119, 1755, 3656, 2126, 244, 3347, 157, 865},
            new int[]{2049, 6396, 4111, 6702, 251, 669, 1491, 245, 210, 4314, 6265, 694, 5131, 228, 6195, 6090},
            new int[]{458, 448, 324, 235, 69, 79, 94, 78, 515, 68, 380, 64, 440, 508, 503, 452},
            new int[]{198, 216, 5700, 4212, 2370, 143, 5140, 190, 4934, 539, 5054, 3707, 6121, 5211, 549, 2790},
            new int[]{3021, 3407, 218, 1043, 449, 214, 1594, 3244, 3097, 286, 114, 223, 1214, 3102, 257, 3345}
        };
        System.out.println("day 2, part1:" + day2_part1(a));
        System.out.println("day 2, part2:" + day2_part2(a));
    }

    private static int day1(String s, int stepSize) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            int j = Math.floorMod(i + stepSize, s.length());
            if (s.charAt(i) == s.charAt(j)) {
                sum += Character.getNumericValue(s.charAt(i));
            }
        }
        return sum;
    }

    private static int day2_part1(int[][] input) {
        int sum = 0;
        for (int[] array : input) {
            sum += max(array) - min(array);
        }
        return sum;
    }

    private static int day2_part2(int[][] input) {
        int sum = 0;
        for (int[] array : input) {
            for (int i = 0; i + 1 < array.length; i++) {
                int a = array[i];
                for (int j = i + 1; j < array.length; j++) {
                    int b = array[j];
                    if (a % b == 0) {
                        sum += a / b;
                        break;
                    } else if (b % a == 0) {
                        sum += b / a;
                        break;
                    }
                }
            }
        }
        return sum;
    }

    private static int min(int... values) {
        int min = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        return min;
    }

    private static int max(int... values) {
        int max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }
}
