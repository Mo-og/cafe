package ua.cafe.utils;

public class Translit {
    public static String cyr2lat(char ch){
        return switch (ch) {
            case 'А' -> "A";
            case 'Б' -> "B";
            case 'В' -> "V";
            case 'Г' -> "G";
            case 'Д' -> "D";
            case 'Е', 'Ё', 'Э' -> "E";
            case 'Ж' -> "Zh";
            case 'З' -> "Z";
            case 'И' -> "I";
            case 'Й' -> "I";
            case 'К' -> "K";
            case 'Л' -> "L";
            case 'М' -> "M";
            case 'Н' -> "N";
            case 'О' -> "O";
            case 'П' -> "P";
            case 'Р' -> "R";
            case 'С' -> "S";
            case 'Т' -> "T";
            case 'У' -> "U";
            case 'Ф' -> "F";
            case 'Х' -> "Kh";
            case 'Ц' -> "Ts";
            case 'Ч' -> "Ch";
            case 'Ш' -> "Sh";
            case 'Щ' -> "Shch";
            case 'Ы' -> "Y";
            case 'Ю' -> "Iu";
            case 'Я' -> "Ia";
            case 'а' -> "a";
            case 'б' -> "b";
            case 'в' -> "v";
            case 'г' -> "g";
            case 'д' -> "d";
            case 'е', 'ё', 'э' -> "e";
            case 'ж' -> "zh";
            case 'з' -> "z";
            case 'и' -> "i";
            case 'й' -> "i";
            case 'к' -> "k";
            case 'л' -> "l";
            case 'м' -> "m";
            case 'н' -> "n";
            case 'о' -> "o";
            case 'п' -> "p";
            case 'р' -> "r";
            case 'с' -> "s";
            case 'т' -> "t";
            case 'у' -> "u";
            case 'ф' -> "f";
            case 'х' -> "kh";
            case 'ц' -> "ts";
            case 'ч' -> "ch";
            case 'ш' -> "sh";
            case 'щ' -> "shch";
            case 'ы' -> "y";
            case 'ю' -> "iu";
            case 'я' -> "ia";
            case '.' -> ".";
            case ',' -> ",";
            default -> "_";
        };
    }

    public static String cyr2lat(String s){
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char ch: s.toCharArray()){
            sb.append(cyr2lat(ch));
        }
        return sb.toString();
    }
}