package br.com.estagio.oletrainning.zup.otmovies.server.response;

public class GenresResponse {

    private final int id;

    private final String name;

    public GenresResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
