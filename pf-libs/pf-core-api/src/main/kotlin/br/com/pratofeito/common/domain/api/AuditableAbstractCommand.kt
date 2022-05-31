package br.com.pratofeito.common.domain.api

import br.com.pratofeito.common.domain.api.model.AuditEntry

abstract class AuditableAbstractCommand(open val auditEntry: AuditEntry)