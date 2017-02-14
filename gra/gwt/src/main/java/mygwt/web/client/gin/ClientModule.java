package mygwt.web.client.gin;

//import com.arcbees.chosen.sample.client.application.ApplicationModule;
import mygwt.web.client.place.NameTokens;
//import com.arcbees.chosen.sample.client.resources.ResourceLoader;
/*
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
*/
public class ClientModule { //extends AbstractPresenterModule {
/*
    @Override
    protected void configure() {
        install(new DefaultModule.Builder().build());
        //install(new ApplicationModule());

        //bind(ResourceLoader.class).asEagerSingleton();

        // DefaultPlaceManager Places !!! absolute must-have for correct compilation
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.CHOSEN_SAMPLE);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.CHOSEN_SAMPLE);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.CHOSEN_SAMPLE);

        // http://stackoverflow.com/questions/23883527/gwtp-no-default-constructor-for-interface
        // bind(PlaceManager.class).to(DefaultPlaceManager.class).in(Singleton.class);
    }
*/
}
