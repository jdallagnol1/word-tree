import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;


public class Terminal {
    LinkedList<Word> lista;
    LinkedList<WordTree> forest;

    public Terminal(){
        forest = new LinkedList<>();
        String[] linhas = new String[1000];
        lista = new LinkedList<>();
        String aux[];
                
        Path path1 = Paths.get("nomes.csv");

        try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                aux = line.split(";");
                Word p = new Word(aux[0],aux[1]);
                lista.add(p);
            }
        } catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: ", e);
        }  
        loadDictionary();
        loadWords();
        menu();
    }
    
    /**
     * Interação com usuario
     */
    public void menu(){
        Scanner in = new Scanner(System.in);
        System.out.println("DICIONARIO\nDigite um conjunto de caracteres para fazer a busca!");
        String prefix = in.nextLine();
        System.out.println("Resultado:\n----------------");
        printWords(prefix);
        System.out.println("Escolha uma das palavras para ver o seu significado!");
        String word = in.nextLine();
        System.out.println("Significado: " + getMeaning(word));
        
    }

    /**
     * Cria uma única arvore para cada letra inicial encontrada na lista de palavras
     */
    public void loadDictionary(){
        for(Word word:lista){
            char[] c = word.getWord().toCharArray();
            if(contains(c[0])==false){
                WordTree wt = new WordTree(c[0]);
                forest.add(wt);
            }
        }
    }
   
    /**
     * Carrega as palavras nas suas respectivas arvores
     */
    public void loadWords(){
        for(Word word:lista){
            char[] c = word.getWord().toCharArray();
            for(int i=0; i<forest.size(); ++i){
                if(forest.get(i).getRoot() - c[0] == 0){
                    WordTree wt = forest.get(i);
                    wt.addWord(c);
                }
            }
        }
    }
    
    /**
     * testa se char c é a raiz da arvore
     * @param c
     * @return boolean
     */
    public boolean contains(char c){
        if(forest.size() == 0){
            return false;
        }
        for(WordTree w:forest){
            if(w.getRoot()==c)
                return true;
        }
        return false;
    }

    /**
     * Busca o significado da palavra
     * @param word
     * @return o significado da palavra 
     */
    public String getMeaning(String word){
        for(Word w:lista){
            if(w.getWord().equals(word)){
                return w.getMeaning();
            }
        }
        return null;
    }

    /**
     * chama metodo da classe WordTree para buscar e imprimir lista de palavras com o prefixo fornecido
     * @param prefix
     */
    public void printWords(String prefix){
        LinkedList<String> list = new LinkedList<>();
        char[] c = prefix.toCharArray();
        for(WordTree w:forest){
            if(w.getRoot() - c[0] == 0){
                list = w.getWordsPrefix(prefix);
            }
        }
        if(list!=null){
            for(String s:list){
                System.out.println(s);
            }
        }
    }
}