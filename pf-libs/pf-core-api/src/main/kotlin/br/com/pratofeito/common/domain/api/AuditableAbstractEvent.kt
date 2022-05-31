package br.com.pratofeito.common.domain.api

import br.com.pratofeito.common.domain.api.model.AuditEntry

abstract class AuditableAbstractEvent(open val auditEntry: AuditEntry)