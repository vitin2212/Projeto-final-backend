package br.pucpr.authserver.mensagens  // Pacote ajustado

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import br.pucpr.authserver.mensagens.MensagemDTO  // Importação ajustada
import br.pucpr.authserver.mensagens.Mensagens  // Importação ajustada
import br.pucpr.authserver.services.MensagemService  // Importação do service

@RestController
@RequestMapping("/mensagens")
class MensagemController(
    val mensagemService: MensagemService  // Injeção de dependência do service
) {

    @PostMapping
    fun criarMensagem(@RequestBody mensagemDto: MensagemDTO): ResponseEntity<Mensagens> {
        // Cria a nova mensagem com base no DTO
        val mensagem = Mensagens(
            emailUser = mensagemDto.emailUser,
            emailDestinatario = mensagemDto.emailDestinatario,
            messageContent = mensagemDto.messageContent
        )

        // Chama o serviço para salvar a mensagem
        val novaMensagem = mensagemService.insert(mensagem)

        return ResponseEntity.ok(novaMensagem)  // Retorna a resposta com a nova mensagem
    }

    @GetMapping("/{remetente}/{destinatario}")
    fun listarMensagens(
        @PathVariable remetente: String,
        @PathVariable destinatario: String
    ): List<Mensagens> {
        // Obtém as mensagens entre os usuários
        val mensagensRemetenteDestinatario = mensagemService.listaEntreUsuarios(remetente, destinatario)
        val mensagensDestinatarioRemetente = mensagemService.listaEntreUsuarios(destinatario, remetente)

        // Combina as duas listas e ordena por ID
        val todasMensagens = (mensagensRemetenteDestinatario + mensagensDestinatarioRemetente).sortedBy { it.id }

        return todasMensagens
    }

    @DeleteMapping("/{id}")
    fun deletarMensagem(@PathVariable id: Long?): ResponseEntity<Void> {
        return if (id != null && mensagemService.deletarMensagem(id)) {
            ResponseEntity.noContent().build<Void>()  // Retorna 204 No Content
        } else {
            ResponseEntity.notFound().build<Void>()  // Retorna 404 Not Found
        }
    }
}
