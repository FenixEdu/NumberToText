package org.fenixedu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;

public class NumberToTextPT {

    private ArrayList<Integer> nro;
    private BigInteger num;
    private BigDecimal valorMonetario;

    private String[] currency;

    private String Qualificadores[][] = { { "cêntimo", "cêntimos" },
            { "", "" }, { "mil", "mil" }, { "milhão", "milhões" },
            { "bilhão", "bilhões" }, { "trilhão", "trilhões" },
            { "quatrilhão", "quatrilhões" }, { "quintilhão", "quintilhões" },
            { "sextilhão", "sextilhões" }, { "septilhão", "septilhões" } };

    private String Numeros[][] = {
            { "zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete",
                    "oito", "nove", "dez", "onze", "doze", "treze", "quatorze",
                    "quinze", "desesseis", "desessete", "dezoito", "desenove" },
            { "vinte", "trinta", "quarenta", "cinquenta", "sessenta",
                    "setenta", "oitenta", "noventa" },
            { "cem", "cento", "duzentos", "trezentos", "quatrocentos",
                    "quinhentos", "seiscentos", "setecentos", "oitocentos",
                    "novecentos" } };


    NumberToTextPT(final String[] currency, final BigDecimal dec) throws NumberFormatException {
        nro = new ArrayList<Integer>();
        this.currency = currency;
        setNumber(dec);
    }

    private void setNumber(BigDecimal dec) throws NumberFormatException {

        // Mantém o valor informado no escopo da classe para utilização
        // posterior
        // pelo método DecimalFormat desta mesma classe.
        valorMonetario = dec;

        // Se o valor informado for negativo ou maior que 999 septilhões,
        // dispara uma exceção.
        BigDecimal maxNumber = new BigDecimal("999999999999999999999999999.99");
        if ((dec.signum() == -1) || (dec.compareTo(maxNumber) == 1)) {
            throw new NumberFormatException(
                    "\nNao sao suportados numeros negativos ou maiores que 999 septilhoes para a conversao de valores monetarios."
                            + "\nNumeros validos vao de 0,00 até 999.999.999.999.999.999.999.999.999,99"
                            + "\nO numero informado foi: " + DecimalFormat());
        }

        // Converte para inteiro arredondando os centavos.
        num = dec.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(
                BigDecimal.valueOf(100)).toBigInteger();

        // Adiciona valores.
        nro.clear();
        if (num.equals(BigInteger.ZERO)) {
            // Centavos.
            nro.add(new Integer(0));
            // Valor.
            nro.add(new Integer(0));
        } else {
            // Adiciona centavos.
            addRemainder(100);

            // Adiciona grupos de 1000.
            while (!num.equals(BigInteger.ZERO)) {
                addRemainder(1000);
            }
        }
    }

    private String DecimalFormat() {
        try (Formatter formatter = new Formatter()) {
            DecimalFormatSymbols sym = new DecimalFormatSymbols();
            Object[] objs = new Object[1];
            objs[0] = valorMonetario;
            formatter.format("%-,27.2f", objs);
            return sym.getCurrencySymbol() + " " + formatter.toString();            
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        int ct;

        for (ct = nro.size() - 1; ct > 0; ct--) {
            // Se ja existe texto e o atual não é zero
            if (buf.length() > 0 && !ehGrupoZero(ct)) {
                buf.append(" e ");
            }
            buf.append(numToString(nro.get(ct).intValue(), ct));
        }
        if (buf.length() > 0) {
            if (ehUnicoGrupo())
                buf.append(" de ");
            while (buf.toString().endsWith(" "))
                buf.setLength(buf.length() - 1);
            if (ehPrimeiroGrupoUm()) {
//                buf.insert(0, "h");
            }
            buf.append(" ");
            if (nro.size() == 2 && nro.get(1).intValue() == 1) {
                buf.append(currency[0]);
            } else {
                buf.append(currency[1]);
            }
            if (nro.get(0).intValue() != 0) {
                buf.append(" e ");
            }
        }
        if (nro.get(0).intValue() != 0) {
            buf.append(numToString(nro.get(0).intValue(), 0));
        }
        return buf.toString();
    }

    private boolean ehPrimeiroGrupoUm() {
        return nro.get(nro.size() - 1).intValue() == 1;
    }

    private void addRemainder(int divisor) {
        // Encontra newNum[0] = num módulo divisor, newNum[1] = num dividido
        // divisor.
        BigInteger[] newNum = num.divideAndRemainder(BigInteger
                .valueOf(divisor));

        // Adiciona módulo.
        nro.add(new Integer(newNum[1].intValue()));

        // Altera número.
        num = newNum[0];
    }

    private boolean ehUnicoGrupo() {
        if (nro.size() <= 3)
            return false;
        if (!ehGrupoZero(1) && !ehGrupoZero(2))
            return false;
        boolean hasOne = false;
        for (int i = 3; i < nro.size(); i++) {
            if (nro.get(i).intValue() != 0) {
                if (hasOne)
                    return false;
                hasOne = true;
            }
        }
        return true;
    }

    private boolean ehGrupoZero(int ps) {
        return ps <= 0 || ps >= nro.size() || nro.get(ps).intValue() == 0;
    }

    private String numToString(int numero, int escala) {
        int unidade = (numero % 10);
        int dezena = (numero % 100); // * nao pode dividir por 10 pois
                                        // verifica
        // de 0..19
        int centena = (numero / 100);

        StringBuffer buf = new StringBuffer();

        if (numero != 0) {
            if (centena != 0) {
                if (dezena == 0 && centena == 1) {
                    buf.append(Numeros[2][0]);
                } else {
                    buf.append(Numeros[2][centena]);
                }
            }

            if ((buf.length() > 0) && (dezena != 0)) {
                buf.append(" e ");
            }
            if (dezena > 19) {
                dezena /= 10;
                buf.append(Numeros[1][dezena - 2]);
                if (unidade != 0) {
                    buf.append(" e ");
                    buf.append(Numeros[0][unidade]);
                }
            } else if (centena == 0 || dezena != 0) {
                buf.append(Numeros[0][dezena]);
            }

            buf.append(" ");
            if (numero == 1) {
                buf.append(Qualificadores[escala][0]);
            } else {
                buf.append(Qualificadores[escala][1]);
            }
        }

        return buf.toString();
    }

}
