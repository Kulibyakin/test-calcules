import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Calcules {
    public static void main(String[] args) {
        String dataString = getData();
        int result = calc(dataString);
        String out = checkResult(dataString, result);
        System.out.println("Результат операции: "+dataString+"="+out);
    }
    public static String convArabToRome(int inputData) {
        String[] Rome = { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M" };
        int[] Arab = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 };
        StringBuilder buff = new StringBuilder();
        int times = 0;
        for (int i = Arab.length - 1; i >= 0; i--) {
            times = inputData / Arab[i];
            inputData %= Arab[i];
            while (times > 0) {
                buff.append(Rome[i]);
                times--;
            }
        }
        return buff.toString();
    }
    public static int convRomeToArab(String inputData) {
        String[] Rome = { "X", "IX", "V", "IV", "I" };
        int[] Arab = { 10, 9, 5, 4, 1 };
        StringBuffer buff = new StringBuffer(inputData);
        int arabNum = 0;
        int i = 0;
        while (true) {
            do {
                if (Rome[i].length() <= buff.length()) {
                    if (Rome[i].equals(buff.substring(0, Rome[i].length()))) {
                        arabNum += Arab[i];
                        buff.delete(0, Rome[i].length());
                        if (buff.length() == 0) return arabNum;
                    } else break;
                } else break;
            } while (true && buff.length() != 0);
            i++;
        }
    }
    public static boolean checkData(String inputData) {
        try{
            // проверка на соответствие символов:
            Pattern pattern = Pattern.compile("[-+*/0-9IVX]*");
            Matcher matcher = pattern.matcher(inputData);
            if (! matcher.matches()) throw new Exception("Ввведены неверные символы");

            // проверка на соответствие формата записи:
            pattern = Pattern.compile("^[-+*/].*|.*[-+*/].*[-+*/]$|.*[0-9][IVX].*|.*[IVX][0-9].*");
            matcher = pattern.matcher(inputData);
            if (matcher.matches()) throw new Exception("Формат математической операции неверен");

            // делим данные на массивы:
            String[] datas = inputData.split("\\+|\\-|\\/|\\*");
            int i = 0;
            int num = 0;    // переменная для определения размера числа
            int numType = 0;    // тип числа: 1 - арабское, 2 - римское
            // определяем типы данных и разбиваем по массивам:
            for(String data : datas) {
                // определение типа числа и его размера:
                pattern = Pattern.compile("[0-9]*");
                matcher = pattern.matcher(data);
                if (matcher.matches()) {
                    if ( numType == 2 ) throw new Exception("используются одновременно разные системы счисления");
                    numType = 1;
                    num = Integer.parseInt(data);
                } else {
                    if ( numType == 1 ) throw new Exception("используются одновременно разные системы счисления");
                    numType = 2;
                    num = convRomeToArab(data);
                }
                if ( 1 > num | num > 10 ) throw new Exception("введеное значение числа вышло за пределы задания");
                i++;
            }

            // проверка условий математики:
            if ( i < 2 ) throw new Exception("Cтрока не является математической операцией");
            if ( i > 2 ) throw new Exception("Формат математической операции не удовлетворяет заданию");

            return true;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
            return false;
        }
    }
    public static String getData() {
        String inputData = null;
        boolean check = false;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Введите выражение: ");
            inputData = in.nextLine();
            check = checkData(inputData);
            if (check) break;
        }
        in.close();
        return inputData;
    }
    public static int calc(String inputData) {
        int result = 0;
        int[] num = new int[2]; // массив чисел
        // делим данные на массивы:
        String[] datas = inputData.split("\\+|\\-|\\/|\\*");
        int i = 0;
        int numType = 0;    // тип числа: 1 - арабское, 2 - римское
        // определяем типы данных и разбиваем по массивам:
        for (String data : datas) {
            // определение типа числа:
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher matcher = pattern.matcher(data);
            if (matcher.matches()) {
                numType = 1;
                num[i] = Integer.parseInt(data);
            } else {
                numType = 2;
                num[i] = convRomeToArab(data);
            }
            i++;
        }
        if (inputData.indexOf('-') > 0) {
            result = num[0] - num[1];
        } else if (inputData.indexOf('+') > 0) {
            result = num[0] + num[1];
        } else if (inputData.indexOf('*') > 0) {
            result = num[0] * num[1];
        } else if (inputData.indexOf('/') > 0) {
            result = num[0] / num[1];
        }
        return result;
    }
    public static String checkResult(String inputData, int result) {
        try{
            // делим данные на массивы:
            String[] datas = inputData.split("\\+|\\-|\\/|\\*");
            int i = 0;
            int num = 0;    // переменная для определения размера числа
            int numType = 0;    // тип числа: 1 - арабское, 2 - римское
            // определяем типы данных и разбиваем по массивам:
            for (String data : datas) {
                // определение типа числа:
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher matcher = pattern.matcher(data);
                if (matcher.matches()) {
                    numType = 1;
                } else {
                    numType = 2;
                }
                i++;
            }
            if ( numType == 2 ) {
                if ( result < 0 ) throw new Exception("в римской системе нет отрицательных чисел");
                if ( result == 0 ) {
                    System.out.println("в римской системе нет нуля, " +
                            "но я могу добавить староримское исчисление");
                    return "N";
                }
                return convArabToRome(result);
            } else return Integer.toString(result);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
            return Integer.toString(result);
        }
    }
}