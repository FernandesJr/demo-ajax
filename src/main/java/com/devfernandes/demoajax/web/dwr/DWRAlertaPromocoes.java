package com.devfernandes.demoajax.web.dwr;

import com.devfernandes.demoajax.repository.PromocaoRepository;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RemoteProxy
@Component
public class DWRAlertaPromocoes {

    @Autowired
    private PromocaoRepository repository;

    private Timer timer;

    public LocalDateTime getUltimaDataCadastroByPromocao(){
        PageRequest pageRequest = PageRequest.of(0,1, Sort.Direction.DESC, "DataCadastro");
        return repository.findUltimaDataDePromocao(pageRequest)
                .getContent()
                .get(0);
    }

    //Thed
    @RemoteMethod
    public synchronized void init(){

        //Método que tem a conexão com o Js
        System.out.println("Iniciando DWR...");

        LocalDateTime lastDate = getUltimaDataCadastroByPromocao();
        WebContext context = WebContextFactory.get();

        //Agendamento de tarefa
        timer = new Timer();
        timer.schedule(new AlertTask(lastDate, context), 10000, 60000); //Tarefa, inicio, ciclo


    }

    class AlertTask extends TimerTask {

        private LocalDateTime lastDate;
        private WebContext context;
        private Long count;

        public AlertTask(LocalDateTime lastDate, WebContext context) {
            this.lastDate = lastDate;
            this.context = context;
        }

        @Override
        public void run() {
            String session = context.getScriptSession().getId(); //Capturando o id da sessão
            //Indicando para onde exatamente deve verificar e enviar a informação
            Browser.withSession(context, session, new Runnable() {
                @Override
                public void run() {
                    //Ao Js chamar o init ele irá verificar qual é a última data de cadastro de promoções
                    //Após o 1 min uma nova tarefa inicia
                    //Ao chegar aqui eu verifico novamente se a data que o init tem realmente é a última do DB
                    //Se não for ele substitui a lastDate e avisa através do count quantas têm disponíveis
                    Map<String, Object> map = repository.totalAndTotalDePromocaoByDataCadastro(lastDate);
                    count = (Long) map.get("count");
                    lastDate = map.get("lastDate") == null ? lastDate : (LocalDateTime) map.get("lastDate");


                    //Log para visualização
                    Calendar time = Calendar.getInstance();
                    time.setTimeInMillis(context.getScriptSession().getLastAccessedTime());
                    System.out.println("count: " + count + "lastDate: " + lastDate  +
                            "< " + session + " >" + "< " + time.getTime() + " >");


                    if(count > 0){
                        //Enviando para o front
                        ScriptSessions.addFunctionCall("showButton", count);
                    }
                }
            });
        }
    }
}
