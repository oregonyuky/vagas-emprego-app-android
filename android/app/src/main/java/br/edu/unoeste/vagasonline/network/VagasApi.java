package br.edu.unoeste.vagasonline.network;

import br.edu.unoeste.vagasonline.model.InteresseRequest;
import br.edu.unoeste.vagasonline.model.Vaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface VagasApi {
    @GET("apis/vagas/get-all")
    Call<List<Vaga>> listarVagas();

    @POST("apis/interesse/candidato")
    Call<Void> registrarInteresse(@Body InteresseRequest interesse);
}
