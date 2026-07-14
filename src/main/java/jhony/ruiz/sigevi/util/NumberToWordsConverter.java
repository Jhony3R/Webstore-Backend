package jhony.ruiz.sigevi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberToWordsConverter {

  private static final String[] UNIDADES = {"", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};
  private static final String[] DECENAS_ESPECIALES = {"DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};
  private static final String[] DECENAS = {"", "", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
  private static final String[] CENTENAS = {"", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};

  public static String convertirNumeroALetras(Double numero) {
    if (numero == null) {
      return ""; // O lanzar una excepción si un número nulo no es válido
    }

    BigDecimal bd = BigDecimal.valueOf(numero).setScale(2, RoundingMode.HALF_UP);
    int parteEntera = bd.intValue();
    int centavos = bd.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

    String letras = "";

    if (parteEntera == 0) {
      letras = "CERO";
    } else {
      if (parteEntera >= 1000000) { // Millones
        int millones = parteEntera / 1000000;
        if (millones == 1) {
          letras += "UN MILLÓN ";
        } else {
          letras += convertirCentenas(millones) + " MILLONES ";
        }
        parteEntera %= 1000000;
      }

      if (parteEntera >= 1000) { // Miles
        int miles = parteEntera / 1000;
        if (miles == 1) {
          letras += "MIL ";
        } else {
          letras += convertirCentenas(miles) + " MIL ";
        }
        parteEntera %= 1000;
      }

      if (parteEntera > 0) { // Cientos, Decenas, Unidades
        letras += convertirCentenas(parteEntera);
      }
    }

    return letras.trim() + " CON " + String.format("%02d", centavos) + "/100 SOLES";
  }

  private static String convertirCentenas(int numero) {
    if (numero == 0) return "";
    if (numero == 100) return "CIEN";
    if (numero < 10) return UNIDADES[numero];
    if (numero < 100) return convertirDecenas(numero);

    int centenas = numero / 100;
    int resto = numero % 100;
    String resultado = CENTENAS[centenas];
    if (resto > 0) {
      resultado += " " + convertirDecenas(resto);
    }
    return resultado;
  }

  private static String convertirDecenas(int numero) {
    if (numero < 10) return UNIDADES[numero];
    if (numero < 20) return DECENAS_ESPECIALES[numero - 10]; // Números del 10 al 19

    int decenas = numero / 10;
    int unidades = numero % 10;
    String resultado = DECENAS[decenas];
    if (unidades > 0) {
      if (decenas == 2) { // "VEINTIUNO", "VEINTIDÓS", etc.
        resultado = "VEINTI" + UNIDADES[unidades];
      } else { // "TREINTA Y UNO", "CUARENTA Y DOS", etc.
        resultado += " Y " + UNIDADES[unidades];
      }
    }
    return resultado;
  }
}

