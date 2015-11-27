package io.sphere.sdk.payments;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;
import java.time.ZonedDateTime;

public final class TransactionDraftBuilder extends Base implements Builder<TransactionDraft> {
    private ZonedDateTime timestamp;
    private TransactionType type;
    private MonetaryAmount amount;
    @Nullable
    private String interactionId;
    @Nullable
    private TransactionState state;

    private TransactionDraftBuilder(final TransactionType type, final MonetaryAmount amount, final ZonedDateTime timestamp, final TransactionState state) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.state = state;
    }

    public static TransactionDraftBuilder of(final TransactionType type, final MonetaryAmount amount, final ZonedDateTime timestamp) {
        return new TransactionDraftBuilder(type, amount, timestamp, null);
    }

    public TransactionDraftBuilder timestamp(final ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public TransactionDraftBuilder interactionId(@Nullable final String interactionId) {
        this.interactionId = interactionId;
        return this;
    }

    public TransactionDraftBuilder state(@Nullable final TransactionState state) {
        this.state = state;
        return this;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    @Nullable
    public String getInteractionId() {
        return interactionId;
    }

    @Nullable
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    @Nullable
    public TransactionState getState() {
        return state;
    }

    @Override
    public TransactionDraft build() {
        return new TransactionDraft(timestamp, type, amount, interactionId, state);
    }
}
