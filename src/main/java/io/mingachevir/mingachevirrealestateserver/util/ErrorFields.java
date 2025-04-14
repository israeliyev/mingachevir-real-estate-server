package io.mingachevir.mingachevirrealestateserver.util;

import java.io.Serializable;

public class ErrorFields implements Serializable {
    private static final long serialVersionUID = 8427085051148563777L;
    private String objectName;
    private String fieldName;
    private String message;
    private String passedArgument;

    public String getObjectName() {
        return this.objectName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPassedArgument() {
        return this.passedArgument;
    }

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setPassedArgument(final String passedArgument) {
        this.passedArgument = passedArgument;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorFields)) {
            return false;
        } else {
            ErrorFields other = (ErrorFields)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$objectName = this.getObjectName();
                Object other$objectName = other.getObjectName();
                if (this$objectName == null) {
                    if (other$objectName != null) {
                        return false;
                    }
                } else if (!this$objectName.equals(other$objectName)) {
                    return false;
                }

                Object this$fieldName = this.getFieldName();
                Object other$fieldName = other.getFieldName();
                if (this$fieldName == null) {
                    if (other$fieldName != null) {
                        return false;
                    }
                } else if (!this$fieldName.equals(other$fieldName)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$passedArgument = this.getPassedArgument();
                Object other$passedArgument = other.getPassedArgument();
                if (this$passedArgument == null) {
                    if (other$passedArgument != null) {
                        return false;
                    }
                } else if (!this$passedArgument.equals(other$passedArgument)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ErrorFields;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $objectName = this.getObjectName();
        result = result * 59 + ($objectName == null ? 43 : $objectName.hashCode());
        Object $fieldName = this.getFieldName();
        result = result * 59 + ($fieldName == null ? 43 : $fieldName.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $passedArgument = this.getPassedArgument();
        result = result * 59 + ($passedArgument == null ? 43 : $passedArgument.hashCode());
        return result;
    }

    public String toString() {
        return "ErrorFields(objectName=" + this.getObjectName() + ", fieldName=" + this.getFieldName() + ", message=" + this.getMessage() + ", passedArgument=" + this.getPassedArgument() + ")";
    }

    public ErrorFields() {
    }

    private ErrorFields(final String objectName, final String fieldName, final String message, final String passedArgument) {
        this.objectName = objectName;
        this.fieldName = fieldName;
        this.message = message;
        this.passedArgument = passedArgument;
    }

    public static ErrorFields of(final String objectName, final String fieldName, final String message, final String passedArgument) {
        return new ErrorFields(objectName, fieldName, message, passedArgument);
    }
}
