// 1232123 - a, c, e, f, i
// 1232075 - b, d, g, h, j

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NA_1232075_1232123 {
    static final int KMMAXBATERIA = 100;
    static final double CUSTOCARREGAMENTO = 5.5;
    static final int DIAPREVENCAO = 4;
    static final String NOMEFICHEIROINPUT = "ficheiro3.txt";

    public static void main(String[] args) throws FileNotFoundException {
        // a
        int[][] matrizPlaneamento = obterInformacao();
        // d
        somarKmTotalAPercorrerPorCadaVeiculo(matrizPlaneamento);
        // c
        int[][] matrizCarregamentos = calcularNumeroRecargasDaBateriaPorDiaPorCarro(matrizPlaneamento);
        // d
        double[][] matrizPorcentagemBateria = calcularPercentagemDaBateria(matrizPlaneamento);
        // e)
        double[] arrMediaKmPercorridos = calcularMediaKmPercorridosPorDia(matrizPlaneamento);
        // f)
        mostrarVeiculosKmSempreAcimaDaMedia(matrizPlaneamento, arrMediaKmPercorridos);
        // g)
        preencherArrayComMaximaSequenciaDeRecargasPorVeiculo(matrizCarregamentos);
        // h)
        exibirDiaMaisTardioEmQueTodosOsVeiculosRecarregam(matrizCarregamentos);
        // i)
        calcularCustoEstimadoRecarregarTodaFrota(matrizCarregamentos);
        // j)
        obterVeiculoDePrevencao(matrizPlaneamento, matrizPorcentagemBateria);
    }

    // method para o header das tabelas
    public static void exibirCabecalhoDaTabela(int[][] matrizResultados, String titulo) {
        System.out.printf("%s%ndia :", titulo);
        for (int linha = 0; linha < matrizResultados[0].length; linha++) {
            System.out.printf("%8d", linha);
        }
        System.out.printf("%n----|--------|");
        for (int veiculo = 0; veiculo < matrizResultados[0].length - 1; veiculo++) {
            System.out.print("-------|");
        }
        System.out.println();
    }

    // exibe valores da tabela da alinea a) e c)
    public static void exibirValoresDaTabelaPlaneamentoERecargas(int[][] matriz) {
        for (int veiculo = 0; veiculo < matriz.length; veiculo++) {
            System.out.printf("V%d  :", veiculo);
            for (int dia = 0; dia < matriz[0].length; dia++) {
                System.out.printf("%8d", matriz[veiculo][dia]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // a
    public static int[][] obterInformacao() throws FileNotFoundException {
        File inputFile = new File(NOMEFICHEIROINPUT);
        Scanner sc = new Scanner(inputFile);
        String descPlaneamento = sc.nextLine();
        int numVeiculos = sc.nextInt();
        int numDiasPlaneamento = sc.nextInt();
        int[][] matriz = new int[numVeiculos][numDiasPlaneamento];
        for (int veiculo = 0; veiculo < numVeiculos; veiculo++) {
            for (int dia = 0; dia < numDiasPlaneamento; dia++) {
                matriz[veiculo][dia] = sc.nextInt();
            }
        }
        sc.close();
        exibirCabecalhoDaTabela(matriz, "a) planeamento (km/dia/veículo)");
        exibirValoresDaTabelaPlaneamentoERecargas(matriz);
        return matriz;

    }

    // b
    public static void somarKmTotalAPercorrerPorCadaVeiculo(int[][] matrizPlaneamento) {
        int nveiculos = matrizPlaneamento.length;
        int[] totalKmPercorrerPorVeiculo = new int[nveiculos];
        for (int veiculo = 0; veiculo < matrizPlaneamento.length; veiculo++) {
            for (int dia = 0; dia < matrizPlaneamento[0].length; dia++) {
                totalKmPercorrerPorVeiculo[veiculo] += matrizPlaneamento[veiculo][dia];
            }
        }
        exibirKmAPercorrerPorVeiculo(totalKmPercorrerPorVeiculo, "b) total de km a percorrer");
    }

    public static void exibirKmAPercorrerPorVeiculo(int[] arr, String titulo) {
        System.out.println(titulo);
        for (int veiculo = 0; veiculo < arr.length; veiculo++) {
            System.out.printf("V%-3d:%8d km%n", veiculo, arr[veiculo]);
        }
        System.out.println();
    }

    // c
    public static int[][] calcularNumeroRecargasDaBateriaPorDiaPorCarro(int[][] matrizPrincipal) {
        int[][] matrizRecargas = new int[matrizPrincipal.length][matrizPrincipal[0].length];
        int somaKms = 0, contadorRecargas = 0;
        for (int veiculo = 0; veiculo < matrizPrincipal.length; veiculo++) {
            for (int dia = 0; dia < matrizPrincipal[0].length; dia++) {
                somaKms = somaKms + matrizPrincipal[veiculo][dia];
                while (somaKms >= KMMAXBATERIA) {
                    somaKms = somaKms - KMMAXBATERIA;
                    contadorRecargas++;
                }
                matrizRecargas[veiculo][dia] = contadorRecargas;
                contadorRecargas = 0;
            }
            somaKms = 0;
        }
        exibirCabecalhoDaTabela(matrizRecargas, "c) recargas das baterias");
        exibirValoresDaTabelaPlaneamentoERecargas(matrizRecargas);
        return matrizRecargas;
    }

    // d
    public static double[][] calcularPercentagemDaBateria(int[][] matriz) {
        double[][] matrizPercentagemBateria = new double[matriz.length][matriz[0].length];
        int kmAPercorrerNoDia = 0;
        double percentagemBateria;
        for (int veiculo = 0; veiculo < matriz.length; veiculo++) {
            int kmDeBateriaQueSobramParaDiaSeguinte = 0;
            for (int dia = 0; dia < matriz[0].length; dia++) {
                kmAPercorrerNoDia = matriz[veiculo][dia] + kmDeBateriaQueSobramParaDiaSeguinte;
                while (kmAPercorrerNoDia >= KMMAXBATERIA) {
                    kmAPercorrerNoDia -= KMMAXBATERIA;
                }
                percentagemBateria = KMMAXBATERIA - kmAPercorrerNoDia;
                matrizPercentagemBateria[veiculo][dia] = percentagemBateria;
                kmDeBateriaQueSobramParaDiaSeguinte = kmAPercorrerNoDia;
            }
        }
        exibirCabecalhoDaTabela(matriz, "d) carga das baterias");
        exibirValoresDaPorcentagemDeBateria(matrizPercentagemBateria);
        return matrizPercentagemBateria;
    }

    // exibe valores da tabela da alinea d)
    public static void exibirValoresDaPorcentagemDeBateria(double[][] matriz) {
        for (int veiculo = 0; veiculo < matriz.length; veiculo++) {
            System.out.printf("V%d  :", veiculo);
            for (int dia = 0; dia < matriz[0].length; dia++) {
                System.out.printf("%7.1f%%", matriz[veiculo][dia]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //e
    public static double[] calcularMediaKmPercorridosPorDia(int[][] matrizPrincipal) {
        int somaDia = 0;
        double[] mediaKmPercorridos = new double[matrizPrincipal[0].length];
        for (int dia = 0; dia < matrizPrincipal[0].length; dia++) {
            for (int veiculo = 0; veiculo < matrizPrincipal.length; veiculo++) {
                somaDia += matrizPrincipal[veiculo][dia];
            }
            mediaKmPercorridos[dia] = ((double) somaDia / matrizPrincipal.length);
            somaDia = 0;
        }
        exibirCabecalhoDaTabela(matrizPrincipal, "e) média de km diários da frota");
        exibirValoresMediaKmPercorridosPorDia(mediaKmPercorridos);
        return mediaKmPercorridos;
    }

    // exibe valores da tabela da alinea e)
    public static void exibirValoresMediaKmPercorridosPorDia(double[] arr) {
        System.out.printf("km  :");
        for (int dia = 0; dia < arr.length; dia++) {
            System.out.printf("%8.1f", arr[dia]);
        }
        System.out.printf("%n%n");
    }

    //f
    public static void mostrarVeiculosKmSempreAcimaDaMedia(int[][] matrizPrincipal, double[] arrMediaKmPercorridos) {
        boolean[] sempreAcimaDaMedia = new boolean[matrizPrincipal.length];
        int quantidadeVeiculosSempreAcimaDaMedia = 0;
        boolean acimaMedia;
        for (int veiculo = 0; veiculo < matrizPrincipal.length; veiculo++) {
            acimaMedia = true;
            for (int dia = 0; dia < matrizPrincipal[0].length; dia++) {
                if (matrizPrincipal[veiculo][dia] <= arrMediaKmPercorridos[dia]) {
                    acimaMedia = false;
                }
            }
            if (acimaMedia) {
                sempreAcimaDaMedia[veiculo] = true;
                quantidadeVeiculosSempreAcimaDaMedia++;
            } else {
                sempreAcimaDaMedia[veiculo] = false;
            }
        }
        exibirVeiculosComKmSempreAcimaDaMedia(sempreAcimaDaMedia, quantidadeVeiculosSempreAcimaDaMedia);
    }

    public static void exibirVeiculosComKmSempreAcimaDaMedia(boolean[] arrSempreAcimaDaMedia, int quantVeiculos) {
        if (quantVeiculos == 0) {
            System.out.print("f) nenhum veículo teve sempre deslocações acima da média diária");
        } else {
            System.out.printf("f) deslocações sempre acima da média diária%n<%d> veículos : ", quantVeiculos);
            for (int veiculo = 0; veiculo < arrSempreAcimaDaMedia.length; veiculo++) {
                if (arrSempreAcimaDaMedia[veiculo]) {
                    System.out.printf("[V%d] ", veiculo);
                }
            }
        }
        System.out.printf("%n%n");
    }

    //g

    public static void preencherArrayComMaximaSequenciaDeRecargasPorVeiculo(int[][] matrizRecargas) {
        int[] recargasConsecutivasDeCadaVeiculo = new int[matrizRecargas.length];
        int maximaSequenciaDeRecargas = 0;
        for (int veiculo = 0; veiculo < matrizRecargas.length; veiculo++) {
            int somaDeRecargasConsecutivas = 0;
            for (int dia = 0; dia < matrizRecargas[0].length; dia++) {
                if (matrizRecargas[veiculo][dia] != 0) {
                    somaDeRecargasConsecutivas += 1;
                } else {
                    somaDeRecargasConsecutivas = 0;
                }
                if (somaDeRecargasConsecutivas > recargasConsecutivasDeCadaVeiculo[veiculo]) {
                    recargasConsecutivasDeCadaVeiculo[veiculo] = somaDeRecargasConsecutivas;
                }
            }
            if (recargasConsecutivasDeCadaVeiculo[veiculo] > maximaSequenciaDeRecargas) {
                maximaSequenciaDeRecargas = recargasConsecutivasDeCadaVeiculo[veiculo];
            }
        }
        exibirVeiculoComMaisDiasConsecutivosDeRecargas(recargasConsecutivasDeCadaVeiculo, maximaSequenciaDeRecargas);
    }

    public static void exibirVeiculoComMaisDiasConsecutivosDeRecargas(int[] arr, int maxSeqDeRecargas) {
        if (maxSeqDeRecargas == 0) {
            System.out.print("g) nenhum veículo recarregou bateria");
        } else {
            System.out.printf("g) veículos com mais dias consecutivas a necessitar de recarga%n<%d> dias consecutivos, veículos : ", maxSeqDeRecargas);
            for (int veiculo = 0; veiculo < arr.length; veiculo++) {
                if (maxSeqDeRecargas == arr[veiculo]) {
                    System.out.printf("[V%d] ", veiculo);
                }
            }
        }
        System.out.printf("%n%n");
    }

    // h
    public static void exibirDiaMaisTardioEmQueTodosOsVeiculosRecarregam(int[][] matriz) {
        boolean todosCarregaram;
        int maiorDia = -1;
        for (int dia = 0; dia < matriz[0].length; dia++) {
            todosCarregaram = true;
            for (int veiculo = 0; veiculo < matriz.length; veiculo++) {
                if (matriz[veiculo][dia] == 0) {
                    todosCarregaram = false;
                }
            }
            if (todosCarregaram) {
                maiorDia = dia;
            }
        }
        if (maiorDia != -1) {
            System.out.printf("h) dia mais tardio em que todos os veículos necessitam de recarregar <%s>", maiorDia);
        } else {
            System.out.print("h) não existem dias em que todos os carros tenham recarregado bateria");
        }
        System.out.printf("%n%n");
    }


    // i
    public static void calcularCustoEstimadoRecarregarTodaFrota(int[][] matrizNumCarregamentos) {
        int somaRecarregamentos = 0;
        int numeroVeiculos = matrizNumCarregamentos.length;
        int quantidadeTotalDias = matrizNumCarregamentos[0].length;
        for (int veiculo = 0; veiculo < numeroVeiculos; veiculo++) {
            for (int dia = 0; dia < quantidadeTotalDias; dia++) {
                somaRecarregamentos += matrizNumCarregamentos[veiculo][dia];
            }
        }
        double custoTotalCarregarFrota = (somaRecarregamentos * CUSTOCARREGAMENTO);
        System.out.printf("i) custo das recargas da frota <%.2f €>%n%n", custoTotalCarregarFrota);
    }

    //j
    public static void obterVeiculoDePrevencao(int[][] matrizPrincipal, double[][] matrizPorcentagemBaterias) {
        int numeroCarros = matrizPrincipal.length;
        int veiculoMenorKm = matrizPrincipal[0][DIAPREVENCAO], idVeiculoMenorKm = 0;
        for (int km = 1; km < numeroCarros; km++) {
            if (matrizPrincipal[km][DIAPREVENCAO] < veiculoMenorKm) {
                veiculoMenorKm = matrizPrincipal[km][DIAPREVENCAO];
                idVeiculoMenorKm = km;
            } else if (matrizPrincipal[km][DIAPREVENCAO] == veiculoMenorKm) {
                    if (matrizPorcentagemBaterias[km][DIAPREVENCAO] > matrizPorcentagemBaterias[idVeiculoMenorKm][DIAPREVENCAO]) {
                        idVeiculoMenorKm = km;
                    }
            }
        }
        System.out.printf("j) veículo de prevenção no dia <%d> : V%d%n", DIAPREVENCAO, idVeiculoMenorKm);
    }
}
