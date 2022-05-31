package br.com.pratofeito.common.domain.api.model

import java.util.Date

data class AuditEntry(val who: String, val `when`: Date)