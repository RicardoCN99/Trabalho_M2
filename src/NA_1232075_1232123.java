// Ricardo_1232075 b) d) g) h) j)
// Bruno_1232123 a) c) e) f) i)

import java.util.Scanner;

public class NA_1232075_1232123 {

    final static int KMMAXBATERIA=100;

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        /* a) */
        int v= sc.nextInt();
        int d= sc.nextInt();

        int[][] matrizPlaneamento= new int[v][d];
        preencheMatrizPlaneamento(sc,matrizPlaneamento);

        /* b) */
        int[] kmAPercorrerPorVeiculo;
        kmAPercorrerPorVeiculo=somaKmAPercorrerPorCadaVeiculo(matrizPlaneamento,v);
        exibeKmAPercorrerPorVeiculo(kmAPercorrerPorVeiculo);

        /* d) */
        double[][] matrizPercentagemBateriasPorDia=new double[v][d];
        verificaPercentagemdaBateria(matrizPlaneamento,matrizPercentagemBateriasPorDia);

        exibeResultadosPercentagemDeBateriaPorDia(matrizPercentagemBateriasPorDia);
        preencheArrayComMaximaSequenciaDeRecargasPorVeiculo(matrizRecargas,veiculos);

        exibirDiaMaisTardioEmQueTodosOsVeiculosRecarregam(matrizRecargas);

    }

    /* a) */
    public static int[][] preencheMatrizPlaneamento(Scanner sc,int[][] matriz){
        for (int v = 0; v < matriz.length; v++) {
            for (int d = 0; d < matriz[0].length; d++) {
            matriz[v][d]= sc.nextInt();
            }
        }
        for (int v = 0; v < matriz.length; v++) {
            for (int d = 0; d < matriz[0].length; d++) {
                System.out.print(matriz[v][d]+" ");
            }
            System.out.println();
        }
        return matriz;
    }


    /* b) */
    public static int[] somaKmAPercorrerPorCadaVeiculo(int[][] matriz,int nveiculos) {
        int[] totalKmPercorrerPorVeiculo=new int[nveiculos];
        for (int v = 0; v < matriz.length; v++) {
            for (int d = 0; d < matriz[0].length; d++) {
                totalKmPercorrerPorVeiculo[v] += matriz[v][d];
            }
        }
        return totalKmPercorrerPorVeiculo;
    }

    public static void exibeKmAPercorrerPorVeiculo(int[] arr){
        System.out.println("b) total de km a percorrer");
        for (int v = 0; v < arr.length; v++) {
            System.out.printf("V%d :%7d km%n",v,arr[v]);
        }
    }

    /* d) */
    public static double[][] verificaPercentagemdaBateria(int[][] matriz,double[][] matrizPercentagemBateria) {
        int kmAPercorrerNoDia = 0;
        double percentagemBateria;
        for (int v = 0; v < matriz.length; v++) {
            int kmDeBateriaQueSobramParaDiaSeguinte=0;
            for (int d = 0; d < matriz[0].length; d++) {
                kmAPercorrerNoDia = matriz[v][d];
                kmAPercorrerNoDia +=kmDeBateriaQueSobramParaDiaSeguinte;
                while (kmAPercorrerNoDia >= KMMAXBATERIA) {
                    kmAPercorrerNoDia -= 100;
                }
                percentagemBateria = 100 - kmAPercorrerNoDia;
                matrizPercentagemBateria[v][d]=percentagemBateria;
                kmDeBateriaQueSobramParaDiaSeguinte= kmAPercorrerNoDia;
            }
        }
        return matrizPercentagemBateria;
    }

    public static void exibeResultadosPercentagemDeBateriaPorDia(double[][] matrizPercentagemDeBateria){
        System.out.println("d) cargas das baterias:");
        for (int d = 0; d < matrizPercentagemDeBateria[0].length; d++) {
            System.out.print("|--------");
        }
        System.out.println();
        for (int v = 0; v < matrizPercentagemDeBateria.length ; v++) {
            for (int d = 0; d < matrizPercentagemDeBateria[0].length ; d++) {
                System.out.printf("%8.1f%%",matrizPercentagemDeBateria[v][d]);
            }
            System.out.println();
        }
    }

   // g) Mostrar os veículos que terão de ser recarregados em mais dias consecutivos
    public static int[] preencheArrayComMaximaSequenciaDeRecargasPorVeiculo(int[][] matrizRecargas, int veiculos){
        int[] recargasConsecutivasDeCadaVeiculo= new int[veiculos];
        int somaDeRecargas=0;
        int maximaSequenciaDeRecargas=0;
        for (int v = 0; v < matrizRecargas.length; v++) {
            for (int d = 0; d < matrizRecargas[0].length; d++) {
                if (matrizRecargas[v][d]!=0){
                    somaDeRecargas+=1;
                }else{
                    if (somaDeRecargas>maximaSequenciaDeRecargas){
                        maximaSequenciaDeRecargas=somaDeRecargas;
                        somaDeRecargas=0;
                    }
                }
            }
            recargasConsecutivasDeCadaVeiculo[v]=maximaSequenciaDeRecargas;
        }
        exibirVeiculoComMaisDiasConsecutivosDeRecargas(recargasConsecutivasDeCadaVeiculo);
        return recargasConsecutivasDeCadaVeiculo;
    }

    public static void exibirVeiculoComMaisDiasConsecutivosDeRecargas(int[] arr){
        int veiculoComMaiorSequenciaDeRecargas=0;
        int sequenciaMaximaDeRecargas=0;
        int sequenciaDeRecargasDeVeiculoAnterior=0;
        for (int v = 0; v < arr.length; v++) {
            if (sequenciaDeRecargasDeVeiculoAnterior<arr[v]){
                veiculoComMaiorSequenciaDeRecargas=v;
                sequenciaMaximaDeRecargas=arr[v];
            }
        }
        System.out.println("veículos com mais dias consecutivas a necessitar de recarga <"+sequenciaMaximaDeRecargas+"> dias consecutivos, veiculos: [V"+veiculoComMaiorSequenciaDeRecargas+"]");
    }

    // h) Obter e visualizar o dia mais tardio em que todos os veículos necessitarão de recarregar nesse dia ou (-1)
    public static void exibirDiaMaisTardioEmQueTodosOsVeiculosRecarregam(int[][] matriz){
        boolean todosCarregaram;
        int maiorDia = -1;
        for (int d = 0; d < matriz[0].length; d++) {
            todosCarregaram = true;
            for (int v = 0; v < matriz.length; v++) {
                if (matriz[v][d] != 0){
                    todosCarregaram = false;
                }
            }
            if (todosCarregaram){
                maiorDia = d;
            }
        }
        System.out.printf("h) dia mais tardio em que todos os veículos necessitam de recarregar <%s>",maiorDia);
    }
    public static void apagar2teste(){

    }

    public static void apagar(){

    }
}


// j) criar constante para colocar o dia X

