export interface IMuscle2Exercise {
  id?: number;
}

export class Muscle2Exercise implements IMuscle2Exercise {
  constructor(public id?: number) {}
}

export function getMuscle2ExerciseIdentifier(muscle2Exercise: IMuscle2Exercise): number | undefined {
  return muscle2Exercise.id;
}
