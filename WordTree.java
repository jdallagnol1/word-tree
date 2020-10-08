import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

public class WordTree {
    private CharNode root;
    private int totalNodes = 0;
    private int totalWords = 0;

    private class CharNode {
        private char character;
        private CharNode father;
        private LinkedList<CharNode> children;
        
        /**
         * Construtor do nodo
         * @param character
         */
        public CharNode(char character) {
            father = null;
            this.character = character;
            children = new LinkedList<>();
        }

        /**
        * Adiciona um filho (caracter) no nodo. Não pode aceitar caracteres repetidos.
        * @param character - caracter a ser adicionado
        */
        public void addChild (char character) {
            CharNode cNode = new CharNode(character);
            children.add(cNode);
        }
        
        public char getChar(){
            return this.character;
        }

        public void setFather(CharNode c){
            father = c;
        }

        /**
         * Cria lista com todas as folhas de um determinado nodo
         * @return LinkedList de CharNode
         */
        public LinkedList<CharNode> getLeaves(){
            LinkedList leaves = new LinkedList<>();
            if(this.children.isEmpty()){
                leaves.add(this);
            }
            else{
                for(CharNode child:children){
                    leaves.addAll(child.getLeaves());
                }
            }
            return leaves;
        }

        /*
        *Conta o numero de filhos
        * @return inteiro com o numero de filhos 
        */
        public int getNumberOfChildren () {
            return children.size();
        }
        
        /*
        *Retorna o nodo pai
        *@return CharNode pai
        */
        public CharNode getFather(){
            return father;
        }
        
        /**
         * @param index
         * @return referencia para nodo filho de index específico
         */
        public CharNode getChild (int index) {
            if (index<0 || index >children.size()){
               throw new IndexOutOfBoundsException();
            }
            return children.get(index);
        }

        /**
         * Confirma se é pai
         * @param c
         * @return boolean se é o pai
         */
        public boolean isParent(char c){
            for(CharNode child:children){
                if(child.getChar() - c == 0){
                    return true;
                }
            }
            return false;
        }
        
        /**
         * @return Lista com todas as referencias para os nodos filhos
         */
        public List<CharNode> getChildren(){
            return children;
        }

        /**
         * Obtém a palavra correspondente a este nodo, subindo até a raiz da árvore
         * @return a palavra
         */
        private String getWord() { 
            String word = "";
            StringBuilder sb = new StringBuilder();
            boolean isRoot = false;
            boolean last = true;
            CharNode aux = getFather();
            while(!isRoot){
                if(last == true){
                    sb.append(getChar());
                    last=false;
                }
                else {
                    sb.append(aux.getChar());
                    aux = aux.getFather();
                }
                if(aux == null){
                    isRoot = true;
                }
            }
            word = sb.reverse().toString();
            return word;
        }
        
        /**
        * Encontra e retorna o nodo filho que tem determinado caracter.
        * @param character - caracter a ser encontrado.
        * @return CharNode - o nodo encontrado 
        */
        public CharNode findChildChar(char character) { 
            for(CharNode c:children){
                if (c.getChar() - character == 0){
                    return c;
                }
            }
            return null;
        }   
    }

    /**
     * Construtor da arvore
     * @param c
     */
    public WordTree(char c) {
      CharNode rt = new CharNode(c);
      root = rt;
      ++totalNodes;
    }

    /**
     * Retorna a raiz da arvore
     * @return a raiz
     */
    public char getRoot(){
        return this.root.getChar();
    }

    /**
     * Conta o total de palavras da arvore
     * @return numero de palavras presentes na arvore
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * Retorna o numero total de nodos;
     * @return quantidade de nodos
     */
    public int getTotalNodes() {
        return totalNodes;
    }
    
    /**
    *Adiciona palavra na estrutura em árvore
    *@param letters
    */
    public void addWord(char[] letters) {
        ++totalWords;
        CharNode aux = root; 
        for(int i=1; i<letters.length; ++i){
            if(aux.getNumberOfChildren()==0){
                aux.addChild(letters[i]);
                CharNode father = aux;
                aux = aux.findChildChar(letters[i]);
                aux.setFather(father);
            }
            else{
                if(aux.isParent(letters[i])){
                    aux=aux.findChildChar(letters[i]);
                }
                else{
                    aux.addChild(letters[i]);
                    CharNode father = aux;
                    aux = aux.findChildChar(letters[i]);
                    aux.setFather(father);
                }
            }
        }
    }
    
    /**
     * Retorna uma lista com todas as palavras que contem o prefixo fornecido
     * @param prefix
     * @return todas as palavras
     */
    public LinkedList<String> getWordsPrefix(String prefix){
        CharNode aux = root;
        LinkedList<String> list = new LinkedList<>();
        List<CharNode> leaves = new LinkedList<>();
        char[] split = prefix.toCharArray();
        for(int i = 1; i < prefix.length(); ++i){
            aux = aux.findChildChar(split[i]);
            if(aux==null){
                return null;
            }
        }
        leaves = aux.getLeaves();

        for(CharNode cNode:leaves){
            String word = cNode.getWord();
            list.add(word);
        }
        return list;
    }
}
