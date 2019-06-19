

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Iago Sestrem Ochôa
 */
public class AlgoritmoGenético_CaixeiroViajante {

    // Variavel global para definir o numero de cidades   
    public static int NUMERO_CIDADES = 0;
    //Variavel global para definir o tamanho da população
    public static int NUMERO_POPULACAO = 10;

    public static void main(String[] args) {
        //////////////  DEFINIÇÕES //////////////////////
        // Variáveis fixas para mostrar a evolução e definir a taxa de mortalidade
        boolean mostrarEvolucao = true;
        float taxaMortalidade = (float) 0.5;
        //Cria o scanner para leitura dos valores
        Scanner ler = new Scanner(System.in);
        //Variável para definir a quantidade de gerações
        int numeroEvolucoes = 0;
        // Verifica a quantidade de cidades
        System.out.println("Informe o número de cidades: ");
        NUMERO_CIDADES = ler.nextInt();
        //Vetor para atribuir uma letra do alfabeto em ordem crescente as cidades
        String[] cidades = new String[NUMERO_CIDADES];
        // Verifica a quantiade de gerações
        System.out.println("Informe o número de gerações: ");
        numeroEvolucoes = ler.nextInt();
        // Vetor para atribuir letras do alfabeto as cidades em ordem crescente
        String[] alfabeto = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Z"};
        // Popula o vetor de cidades com as letras em ordem crescente
        for (int i = 0; i < NUMERO_CIDADES; i++) {
            cidades[i] = alfabeto[i];
        }
        // Cria a matriz de adjecencia do tamanho dos caminhos entre as cidades
        int mapa[][] = new int[NUMERO_CIDADES][NUMERO_CIDADES];
        // Inicializa a matriz de adjencia em 0
        for (int i = 0; i < NUMERO_CIDADES; i++) {
            for (int j = 0; j < NUMERO_CIDADES; j++) {
                mapa[i][j] = 0;
            }
        }
        // Popula a matriz de adjecencia com os valores informados pelo usuario (replica o triangulo inferior de acordo com o triangulo superior)
        for (int i = 0; i < NUMERO_CIDADES; i++) {
            for (int j = 0; j < NUMERO_CIDADES; j++) {
                if (i == j) {
                    mapa[i][j] = 0;
                } else if (mapa[i][j] == 0) {
                    System.out.println("Informe a distância da cidade " + (cidades[i]) + " com a cidade " + (cidades[j]));
                    mapa[i][j] = ler.nextInt();
                    mapa[j][i] = mapa[i][j];
                }
            }
        }
        // Mostra a matriz de adjecencia no console
        for (int i = 0; i < NUMERO_CIDADES; i++) {
            System.out.println();
            for (int j = 0; j < NUMERO_CIDADES; j++) {
                System.out.printf("%d ", mapa[i][j]);
            }
        }
        System.out.println("\n");
        // Cria os cromossomos do tamanho (linhas = tamanho da população / colunas = tamanho de cidades)
        int[][] cromossomos = new int[NUMERO_POPULACAO][NUMERO_CIDADES];
        // Cria o vetor de resultados com tamanho igual da população
        int[] resultados = new int[NUMERO_POPULACAO];
        // Função para gerar os cromossomos aleatoriamente
        gerarCromossomosAleatoriamente(cromossomos);
        // Função para calcular os resultados
        calcularResultado(cromossomos, resultados, mapa);
        // Função para ordenar os resultados
        ordenar(cromossomos, resultados);
        // Mostra os resultados de acordo com a variavel bool de mostrar resultador (se bool = true mostra o resultado)
        if (mostrarEvolucao) {
            imprimir(cromossomos, resultados, cidades);
        }
        // Refaz todas as funções acima para o número pré-determinado de gerações
        int i;
        for (i = 0; i < numeroEvolucoes; i++) {
            renovarCromossomos(cromossomos, resultados, taxaMortalidade);
            calcularResultado(cromossomos, resultados, mapa);
            ordenar(cromossomos, resultados);
            if (mostrarEvolucao) {
                System.out.println("\nGeracao: " + (i + 1));
                imprimir(cromossomos, resultados, cidades);
            }
        }
        // Mostra os resultados encontrados
        resultado(cromossomos, resultados, cidades);
    }
    //Função para mostrar os resultados
    private static void resultado(int[][] cromossomos, int[] resultados, String[] cidades) {
        int i, i2;
        i = 0;
        for (i2 = 0; i2 < NUMERO_CIDADES; i2++) {
            System.out.print(cidades[cromossomos[i][i2]] + " => ");
        }
        System.out.print(cidades[cromossomos[i][0]] + " ");
        System.out.println(" Resultado: " + resultados[i]);

    }
    // Função para renovar os cromossomos
    public static void renovarCromossomos(int[][] cromossomos, int[] resultados, float taxaMortalidade) {

        int inicioExcluidos = (int) (taxaMortalidade * 10);
        int i, i2 = 0;
        for (i = inicioExcluidos; i < 10; i++) {
        boolean valido = false;
        while (!valido) {
            int[] c_tmp = resetaCromossomo();
            // Pega 2 pais aleatoreamente
             int pai1, pai2;
             pai1 = new Random().nextInt(inicioExcluidos);
             do {
                pai2 = new Random().nextInt(inicioExcluidos);
            } while ((pai1 == pai2) && (resultados[pai1] != resultados[pai2]));
            // Pega caracteristicas do pai 1 aleatoriamente
            for (i2 = 0; i2 < (int) NUMERO_CIDADES / 4; i2++) {
                int pos;
                pos = new Random().nextInt(NUMERO_CIDADES);
                c_tmp[pos] = cromossomos[pai1][pos];
            }
            //Pega características do pai 2
            for (i2 = 0; i2 < (int) NUMERO_CIDADES / 4; i2++) {
                int pos = new Random().nextInt(NUMERO_CIDADES);
                    if (c_tmp[pos] == -1) {
                        if (valorValidoNoCromossomo(cromossomos[pai2][pos],c_tmp)) {
                            c_tmp[pos] = cromossomos[pai2][pos];
                        }
                    }
            }
            //Preenche as características restantes com aleatórios
            for (i2 = 0; i2 < NUMERO_CIDADES; i2++) {
                if (c_tmp[i2] == -1) {
                    int crom_temp = valorValidoNoCromossomo(c_tmp);
                    c_tmp[i2] = crom_temp;
                }
            }
            // Verifica se é valido
                valido = cromossomoValido(c_tmp, cromossomos);
                if (valido) {
                    cromossomos[i] = c_tmp;
                }
            }
        }

    }
    // Função para gerar os cromossomos aleatoriamente
    private static int[][] gerarCromossomosAleatoriamente(int[][] cromossomos) {

        // Inicializa os cromossomos aleatoriamente
        int[] c_tmp = new int[NUMERO_CIDADES];
        int i, i2;
        for (i = 0; i < NUMERO_POPULACAO; i++) {
            boolean crom_valido = false;
            while (!crom_valido) {
                crom_valido = true;
                c_tmp = resetaCromossomo();

                // Gera os cromossomos
                for (i2 = 0; i2 < NUMERO_CIDADES; i2++) {
                    c_tmp[i2] = valorValidoNoCromossomo(c_tmp);
                }
                crom_valido = cromossomoValido(c_tmp, cromossomos);
            }
            cromossomos[i] = c_tmp;
        }
        return cromossomos;
    }
    // Função para "resetar" o cromossomo (atribui valor -1 para ele)
    private static int[] resetaCromossomo() {
        int[] c = new int[NUMERO_CIDADES];
        int i;
        for (i = 0; i < NUMERO_CIDADES; i++) {
            c[i] = -1;
        }
        return c;
    }
    // Função pai 1 para gerar um valor valido no cromossomo
    private static int valorValidoNoCromossomo(int[] c_tmp) {
        int crom_temp;
        boolean valido;
        do {
            crom_temp = new Random().nextInt(NUMERO_CIDADES);
            valido = true;
            for (int ii = 0; ii < NUMERO_CIDADES; ii++) {
                if (c_tmp[ii] == crom_temp) {
                    valido = false;
                }
            }
        } while (!valido);
        return crom_temp;
    }
    // Função pai 2 para gerar um valor valido no cromossomo
    private static boolean valorValidoNoCromossomo(int valor, int[] c_tmp) {
        int crom_temp = valor;
        boolean valido;

        valido = true;
        for (int ii = 0; ii < NUMERO_CIDADES; ii++) {
            if (c_tmp[ii] == crom_temp) {
                valido = false;
            }
        }

        return valido;
    }
    //Função para verificar se o cromossomo é  valido
    private static boolean cromossomoValido(int[] c_tmp, int[][] cromossomos) {
        int j, j2;
        boolean crom_valido = true;
        for (j = 0; j < NUMERO_POPULACAO; j++) {
            int n_iguais = 0;
            for (j2 = 0; j2 < NUMERO_CIDADES; j2++) {
                if (c_tmp[j2] == cromossomos[j][j2]) {
                    n_iguais++;
                }
            }
            if (n_iguais == NUMERO_CIDADES) {
                crom_valido = false;
            }
        }
        return crom_valido;
    }
    //Função para imprimir os resultados
    private static void imprimir(int[][] cromossomos, int[] resultados,String[] cidades) {
        int i, i2;
        for (i = 0; i < NUMERO_POPULACAO; i++) {
            for (i2 = 0; i2 < NUMERO_CIDADES; i2++) {
                System.out.print(cidades[cromossomos[i][i2]] + " => ");
            }
            System.out.print(cidades[cromossomos[i][0]] + " ");
            System.out.println(" Resultados: " + resultados[i]);
        }
    }
    // Função para calcular o resultado/ fitness
    private static void calcularResultado(int[][] cromossomos, int[] resultados, int[][] mapa) {
        int i, i2;
        // calculando o resultado
        for (i = 0; i < NUMERO_POPULACAO; i++) {
            int resTmp = 0;
            for (i2 = 0; i2 < NUMERO_CIDADES - 1; i2++) {
                resTmp += mapa[cromossomos[i][i2]][cromossomos[i][i2 + 1]];
            }
            resTmp += mapa[cromossomos[i][0]][cromossomos[i][i2]];
            resultados[i] = resTmp;
        }

    }
    // Função para ordenar os resultados
    private static void ordenar(int[][] cromossomos, int[] resultados) {
        // ordenando
        int i, i2;
        for (i = 0; i < 10; i++) {
            for (i2 = i; i2 < 10; i2++) {
                if (resultados[i] > resultados[i2]) {
                    int vTmp;
                    int[] vvTmp = new int[10];
                    vTmp = resultados[i];
                    resultados[i] = resultados[i2];
                    resultados[i2] = vTmp;
                    vvTmp = cromossomos[i];
                    cromossomos[i] = cromossomos[i2];
                    cromossomos[i2] = vvTmp;
                }
            }

        }
    }
}