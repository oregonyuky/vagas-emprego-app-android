package br.edu.unoeste.vagasonline.network;

import br.edu.unoeste.vagasonline.model.InteresseRequest;
import br.edu.unoeste.vagasonline.model.Vaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface VagasApi {
    @GET("api/vagas")
    Call<List<Vaga>> listarVagas(@Query("cidade") String cidade, @Query("estado") String estado);

    @POST("api/vagas/{id}/interesses")
    Call<Void> registrarInteresse(@Path("id") String vagaId, @Body InteresseRequest interesse);
}
