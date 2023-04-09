package mn.edu.num.stud.enkhjin.memorizeapp;

public class Word {
    private String word;
    private String translatedWord;
    private int id;

    // creating getter and setter methods
    public String getWord() { return word; }

    public void setWord(String word)
    {
        this.word = word;
    }

    public String getTranslatedWord()
    {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord)
    {
        this.translatedWord = translatedWord;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    // constructor
    public Word(String word,
                String translatedWord)
    {
        this.word = word;
        this.translatedWord = translatedWord;
    }
}
