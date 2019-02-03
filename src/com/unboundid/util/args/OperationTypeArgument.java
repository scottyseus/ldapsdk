/*
 * Copyright 2008-2019 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2008-2019 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.util.args;

import com.unboundid.ldap.sdk.OperationType;
import com.unboundid.util.Mutable;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.unboundid.util.args.ArgsMessages.*;

@Mutable()
@ThreadSafety(level= ThreadSafetyLevel.NOT_THREADSAFE)
public final class OperationTypeArgument extends Argument
{

  private Set<OperationType> values;
  private Set<OperationType> defaultValues;

  /**
   * Creates a new string argument with the provided information.
   *
   * @param  shortIdentifier   The short identifier for this argument.  It may
   *                           not be {@code null} if the long identifier is
   *                           {@code null}.
   * @param  longIdentifier    The long identifier for this argument.  It may
   *                           not be {@code null} if the short identifier is
   *                           {@code null}.
   * @param  isRequired        Indicates whether this argument is required to
   *                           be provided.
   * @param  maxOccurrences    The maximum number of times this argument may be
   *                           provided on the command line.  A value less than
   *                           or equal to zero indicates that it may be present
   *                           any number of times.
   * @param  valuePlaceholder  A placeholder to display in usage information to
   *                           indicate that a value must be provided.  It may
   *                           be {@code null} if a default placeholder should
   *                           be used.
   * @param  description       A human-readable description for this argument.
   *                           It must not be {@code null}.
   *
   * @throws  ArgumentException  If there is a problem with the definition of
   *                             this argument.
   */
  public OperationTypeArgument(final Character shortIdentifier,
                               final String longIdentifier,
                               final boolean isRequired,
                               final int maxOccurrences,
                               final String valuePlaceholder,
                               final String description)
       throws ArgumentException
  {
    this(shortIdentifier, longIdentifier, isRequired,
         maxOccurrences, valuePlaceholder, description, null);
  }

  /**
   * Creates a new string argument with the provided information.
   *
   * @param  shortIdentifier   The short identifier for this argument.  It may
   *                           not be {@code null} if the long identifier is
   *                           {@code null}.
   * @param  longIdentifier    The long identifier for this argument.  It may
   *                           not be {@code null} if the short identifier is
   *                           {@code null}.
   * @param  isRequired        Indicates whether this argument is required to
   *                           be provided.
   * @param  maxOccurrences    The maximum number of times this argument may be
   *                           provided on the command line.  A value less than
   *                           or equal to zero indicates that it may be present
   *                           any number of times.
   * @param  valuePlaceholder  A placeholder to display in usage information to
   *                           indicate that a value must be provided.  It may
   *                           be {@code null} if a default placeholder should
   *                           be used.
   * @param  description       A human-readable description for this argument.
   *                           It must not be {@code null}.
   * @param  defaultValues     The set of default values that will be used for
   *                           this argument if no values are provided.
   *
   * @throws  ArgumentException  If there is a problem with the definition of
   *                             this argument.
   */
  public OperationTypeArgument(final Character shortIdentifier,
                               final String longIdentifier,
                               final boolean isRequired,
                               final int maxOccurrences,
                               final String valuePlaceholder,
                               final String description,
                               final Set<OperationType> defaultValues)
       throws ArgumentException
  {
    super(shortIdentifier, longIdentifier, isRequired, maxOccurrences,
         valuePlaceholder, description);
    if(defaultValues != null)
    {
      this.values = defaultValues;
      this.defaultValues = defaultValues;
    } else
    {
      this.values = new HashSet<>();
    }
  }

  /**
   * Creates a new OperationType argument that is a "clean" copy of the
   * provided source argument.
   *
   * @param  source  The source argument to use for this argument.
   */
  private OperationTypeArgument(final OperationTypeArgument source)
  {
    super(source);
    this.defaultValues = source.defaultValues;
    this.values = new HashSet<>();
  }

  @Override
  public List<String> getValueStringRepresentations(final boolean useDefault)
  {
    final List<String> strings = new ArrayList<>(values.size());
    for(final OperationType operationType : values)
    {
      strings.add(operationType.name());
    }
    return strings;
  }

  @Override
  protected void addValue(final String valueString) throws ArgumentException
  {
    final OperationType parsedOperation = OperationType.forName(valueString);
    if(parsedOperation == null) {
      throw new ArgumentException("Invalid value '" + valueString
           + "' specified for " +
           "--authnRequiredOperationType. Please specify a " +
           "valid LDAP operation type" +
           "(e.g. 'bind').");
    }

    values.add(parsedOperation);
  }

  @Override
  protected boolean hasDefaultValue()
  {
    return defaultValues != null && !defaultValues.isEmpty();
  }

  @Override
  public String getDataTypeName()
  {
    return INFO_STRING_TYPE_NAME.get();
  }

  @Override
  public Argument getCleanCopy()
  {
    return new OperationTypeArgument(this);
  }

  @Override
  protected void addToCommandLine(final List<String> argStrings)
  {
    if(values != null)
    {
      for(final OperationType operationType : values)
      {
        argStrings.add(operationType.name());
      }
    }
  }

  @Override
  public void toString(final StringBuilder buffer)
  {
    buffer.append("OperationTypeArgument(");
    appendBasicToStringInfo(buffer);
    buffer.append(")");
  }

  /**
   * Retrieves the set of values for this argument, or the default values if
   * none were provided.
   *
   * @return  The set of values for this argument, or the default values if none
   *          were provided.
   */
  public Collection<OperationType> getValues() {
    return values;
  }
}
