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
            case 'Ж': return "ZH";
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
            case 'Х': return "KH";
            case 'Ц': return "TS";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "SHCH";
            case 'Ъ': return "″";
            case 'Ы': return "Y";
            case 'Ь': return "′";
            case 'Ю': return "IU";
            case 'Я': return "IA";
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