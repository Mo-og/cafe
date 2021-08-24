package ua.cafe;

public class Translit {
    public static String cyr2lat(char ch){
        switch (ch){
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е':
            case 'Ё':
            case 'Э':
                return "E";
            case 'Ж': return "Zh";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "I";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "Kh";
            case 'Ц': return "Ts";
            case 'Ч': return "Ch";
            case 'Ш': return "Sh";
            case 'Щ': return "Shch";
            case 'Ы': return "Y";
            case 'Ю': return "Iu";
            case 'Я': return "Ia";
            case 'а': return "a";
            case 'б': return "b";
            case 'в': return "v";
            case 'г': return "g";
            case 'д': return "d";
            case 'е':
            case 'ё':
            case 'э':
                return "e";
            case 'ж': return "zh";
            case 'з': return "z";
            case 'и': return "i";
            case 'й': return "i";
            case 'к': return "k";
            case 'л': return "l";
            case 'м': return "m";
            case 'н': return "n";
            case 'о': return "o";
            case 'п': return "p";
            case 'р': return "r";
            case 'с': return "s";
            case 'т': return "t";
            case 'у': return "u";
            case 'ф': return "f";
            case 'х': return "kh";
            case 'ц': return "ts";
            case 'ч': return "ch";
            case 'ш': return "sh";
            case 'щ': return "shch";
            case 'ы': return "y";
            case 'ю': return "iu";
            case 'я': return "ia";
            case '.': return ".";
            case ',': return ",";
            default: return "_";
        }
    }

    public static String cyr2lat(String s){
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char ch: s.toCharArray()){
            sb.append(cyr2lat(ch));
        }
        return sb.toString();
    }
}